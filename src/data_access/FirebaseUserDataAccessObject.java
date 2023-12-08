package data_access;

import entities.auth.DisplayUser;
import entities.auth.User;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.login.LoginUserDataAccessInterface;
import use_case.settings.DeleteUserDataAccessInterface;
import use_case.settings.UserSettingsDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

/**
 * The FirebaseUserDataAccessObject class is responsible for signing up, logging in, deleting user, and
 * managing user settings with Firebase and identitytoolkit.
 *
 * This also allows us to manage user details like display user in firebase.
 */
public class FirebaseUserDataAccessObject
    implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        DeleteUserDataAccessInterface,
        UserSettingsDataAccessInterface {

  private final OkHttpClient client;

  /**
   * The FirebaseUserDataAccessObject class is responsible for signing up and logging in with
   * Firebase/identitytoolkit.
   *
   * @param client The OkHttpClient instance used for making HTTP requests.
   */
  public FirebaseUserDataAccessObject(OkHttpClient client) {
    this.client = client;
  }

  /**
   * The internal SignupResults class represents the result of a user signup process. It contains
   * the generated user id and the authentication token.
   */
  private static class SignupResults {
    final String uid;
    final String idToken;

    SignupResults(String uid, String idToken) {
      this.uid = uid;
      this.idToken = idToken;
    }
  }

  /**
   * When a user changes their display name, propagates that change to the Firebase server (not
   * identitytoolkit).
   *
   * @param idToken the authentication token of the user
   * @param user the user object containing the updated display name
   */
  public void propogateDisplayNameChange(String idToken, User user) {
    saveUserToFirebase(user.getEmail(), user.getName(), idToken);
  }

  /**
   * Retrieves the access token for the given email and password by making an authentication request
   * to identitytoolkit.
   *
   * @param email The email address of the user.
   * @param password The password of the user.
   * @return The access token if authentication is successful.
   * @throws RuntimeException If authentication fails or an internal error occurs during
   *     authentication.
   */
  public String getAccessToken(String email, String password) {
    // Authentication Request
    JSONObject jsonBody =
        new JSONObject()
            .put("email", email)
            .put("password", password)
            .put("returnSecureToken", true);

    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

    String authUrl = Constants.LOGIN_URL + "?key=" + Constants.FIREBASE_AUTH_ID;

    Request authRequest = new Request.Builder().url(authUrl).post(body).build();

    try {
      Response authResponse = client.newCall(authRequest).execute();
      String authResponseData = authResponse.body().string();
      JSONObject authResponseJson = new JSONObject(authResponseData);

      if (!authResponse.isSuccessful()) {
        String failureMessage = authResponseJson.getJSONObject("error").getString("message");
        throw new RuntimeException("Authentication failed: " + failureMessage);
      }

      if (!authResponseJson.has("idToken") || !authResponseJson.has("localId")) {
        throw new RuntimeException("Invalid authentication response");
      }

      return authResponseJson.getString("idToken");
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Internal error during authentication, please try again.");
    }
  }

  /**
   * Retrieves the user data for the given email and password by making a request to
   * identitytoolkit.
   *
   * @param email The email address of the user.
   * @param password The password of the user.
   * @return The user if authentication is successful.
   * @throws RuntimeException If authentication fails or an internal error occurs during
   *     authentication.
   */
  @Override
  public User getUser(String idToken, String email, String password) {
    return getPrivateUserData(idToken, password);
  }

  public DisplayUser getDisplayUser(String email) {
    // Note: This method doesn't require authentication, as anyone is allowed to retrieve a display
    // name given an uid
    // In addition, this method returns a RuntimeError if there is no DisplayUser available
    String encodedEmail = Base64.getEncoder().encodeToString(email.toLowerCase().getBytes());
    String url = String.format(Constants.DISPLAY_NAME_URL, encodedEmail);
    Request request = new Request.Builder().url(url).get().build();

    try {
      Response response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        String displayName = response.body().string().replace('"', ' ').trim();
        return new DisplayUser(email, displayName);
      } else {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to retrieve user data. Please try again.");
    }
  }

  /**
   * Retrieves the user data for the given email and password by making a request to
   * identitytoolkit.
   *
   * @return time user was created and uid
   */
  private User getPrivateUserData(String idToken, String password) {
    JSONObject jsonBody = new JSONObject().put("idToken", idToken);

    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

    String url = Constants.ACCOUNT_LOOKUP_URL + "?key=" + Constants.FIREBASE_AUTH_ID;

    Request request = new Request.Builder().url(url).post(body).build();

    try {
      Response response = client.newCall(request).execute();
      String responseData = response.body().string();

      if (!response.isSuccessful()) {
        throw new RuntimeException("User lookup failed, please try again.");
      }

      JSONObject responseObject = new JSONObject(responseData);
      JSONObject userObject = responseObject.getJSONArray("users").getJSONObject(0);

      String uid = userObject.optString("localId");
      String email = userObject.optString("email");
      DisplayUser displayUser = getDisplayUser(email);
      String displayName = displayUser.getName();
      long createdAt = Long.parseLong(userObject.optString("createdAt"));
      LocalDateTime dateTime =
          LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault());

      return new User(uid, email, displayName, password, dateTime);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("User lookup failed, please try again.");
    }
  }

  /**
   * The signup method is responsible for signing up a user by sending a signup request to the
   * Firebase authentication API. It takes a User object as a parameter, which contains the user's
   * email and password.
   *
   * @param user The User object containing the email and password of the user.
   * @return The SignupResults object, which contains the generated user id (uid) and authentication
   *     token (idToken).
   * @throws RuntimeException If an error occurs during the signup process.
   */
  private SignupResults signup(User user) {
    JSONObject jsonBody =
        new JSONObject().put("email", user.getEmail()).put("password", user.getPassword());

    String signupUrl = Constants.SIGNUP_URL + "?key=" + Constants.FIREBASE_AUTH_ID;

    RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json"));

    Request request = new Request.Builder().url(signupUrl).post(body).build();
    try {
      Response response = client.newCall(request).execute();
      JSONObject jsonResponse = new JSONObject(response.body().string());
      if (response.isSuccessful()) {
        String uid = jsonResponse.getString("localId");
        String idToken = jsonResponse.getString("idToken");
        return new SignupResults(uid, idToken);
      } else {
        String error =
            jsonResponse
                .getJSONObject("error")
                .getJSONArray("errors")
                .getJSONObject(0)
                .getString("message");
        throw new RuntimeException("Unable to sign up: " + error);
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unexpected error signing up. Please try again.");
    }
  }

  /**
   * Saves the user's display name to Firebase server.
   *
   * @param email The email address of the user.
   * @param displayName The display name to be saved.
   * @param idToken The authentication token of the user.
   * @throws RuntimeException If there is an error saving the display name.
   */
  private void saveUserToFirebase(String email, String displayName, String idToken) {
    String jsonBody = JSONObject.quote(displayName);
    String encodedEmail = Base64.getEncoder().encodeToString(email.toLowerCase().getBytes());
    String url = String.format(Constants.DISPLAY_NAME_URL, encodedEmail) + "?auth=" + idToken;
    RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

    Request request = new Request.Builder().url(url).put(body).build();
    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to save display name. Please try again.");
    }
  }

  /**
   * Saves the user's data by signing up the user and saving the display name to the Firebase
   * server. This method takes a User object as a parameter and performs the following steps: 1.
   * Calls the private method signup() to sign up the user and obtain the generated user id (uid)
   * and authentication token (idToken). 2. Calls the private method saveUserToFirebase() to save
   * the user's display name to the Firebase server.
   *
   * @param user The User object containing the email, password, and display name of the user.
   * @throws RuntimeException If an error occurs during the signup process or saving the display
   *     name.
   */
  @Override
  public void save(User user) {
    SignupResults signupResults = signup(user);
    saveUserToFirebase(user.getEmail(), user.getName(), signupResults.idToken);
  }

  /**
   * Deletes the user data from Firebase.
   *
   * @param user The User object containing the user's information.
   * @param idToken The authentication token of the user.
   * @throws RuntimeException If unable to delete the user data.
   */
  private void deleteFirebaseUserData(User user, String idToken) {
    String encodedEmail =
        Base64.getEncoder().encodeToString(user.getEmail().toLowerCase().getBytes());
    String url = String.format(Constants.USER_DATA_URL, encodedEmail) + "?auth=" + idToken;
    Request request = new Request.Builder().url(url).delete().build();
    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to delete user data. Please try again.");
    }
  }

  /**
   * Deletes the user data from the Firebase authentication API.
   *
   * @param idToken the authentication token of the user
   * @param user the user object containing the user's information
   * @throws RuntimeException if unable to delete the user data
   */
  private void deleteUserFromAuth(String idToken, User user) {
    JSONObject jsonBody = new JSONObject().put("idToken", idToken);
    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

    String url = Constants.DELETE_USER_URL + "?key=" + Constants.FIREBASE_AUTH_ID;

    Request request = new Request.Builder().url(url).post(body).build();
    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to delete user account. Please try again.");
    }
  }

  /**
   * Deletes the user data from the Firebase authentication API and Firebase database. This method
   * is responsible for deleting a user's data completely from the system.
   *
   * <p>Before calling this method, make sure to remove the user from the rooms UI.
   *
   * @param idToken The authentication token of the user.
   * @param user The User object containing the user's information.
   * @throws RuntimeException If unable to delete the user data.
   */
  @Override
  public void deleteUser(String idToken, User user) {
    // Before calling this method, please remove the user from their rooms using the Rooms DAO
    deleteFirebaseUserData(user, idToken);
    deleteUserFromAuth(idToken, user);
  }
}
