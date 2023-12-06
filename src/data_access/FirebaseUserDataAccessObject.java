package data_access;

import entities.auth.DisplayUser;
import entities.auth.User;
import entities.auth.UserFactory;
import entities.settings.SettingsChangeResponse;
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

public class FirebaseUserDataAccessObject
    implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        DeleteUserDataAccessInterface,
        UserSettingsDataAccessInterface {

  private final OkHttpClient client;

  private String token = null;
  private User user = null;

  public FirebaseUserDataAccessObject(OkHttpClient client) {
    this.client = client;
  }

  private class SignupResults {
    String uid;
    String idToken;

    SignupResults(String uid, String idToken) {
      this.uid = uid;
      this.idToken = idToken;
    }
  }

  public void propogateDisplayNameChange(User user) {
    String idToken = getAccessToken(user.getEmail(), user.getPassword());

    saveUserToFirebase(user.getEmail(), user.getName(), idToken);
  }

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

  @Override
  public User getUser(String email, String password) {
    String idToken = getAccessToken(email, password);
    // Initialize user from our idToken and password by making a second call to Firebase
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

  // Return time user was created and uid
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

      UserFactory userFactory = new UserFactory();
      return userFactory.create(uid, email, displayName, password, dateTime);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("User lookup failed, please try again.");
    }
  }

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

  @Override
  public void save(User user) {
    SignupResults signupResults = signup(user);
    saveUserToFirebase(user.getEmail(), user.getName(), signupResults.idToken);
  }

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

  private void deleteUserFromAuth(User user) {
    String idToken = getAccessToken(user.getEmail(), user.getPassword());
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

  @Override
  public void deleteUser(User user) {
    // TODO: Remove the user from any rooms for which they may be a member
    // Before calling this method, please remove the user from their rooms using the Rooms DAO
    String idToken = getAccessToken(user.getEmail(), user.getPassword());
    deleteFirebaseUserData(user, idToken);
    deleteUserFromAuth(user);
  }
}
