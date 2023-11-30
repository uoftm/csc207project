package data_access;

import entity.CommonUserFactory;
import entity.User;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.chat.ChatUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class FirebaseUserDataAccessObject
    implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        ChatUserDataAccessInterface {

  private final OkHttpClient client;

  private String token = null;
  private User user = null;

  public FirebaseUserDataAccessObject(OkHttpClient client) {
    this.client = client;
  }

  @Override
  public User get(String email, String password) {
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

      String idToken = authResponseJson.getString("idToken");

      // Initialize user from our idToken and password by making a second call to Firebase
      return getUserData(idToken, password);
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Internal error during authentication, please try again.");
    }
  }

  // Return username, time user was created, and uid
  private User getUserData(String idToken, String password) {
    JSONObject jsonBody = new JSONObject();
    jsonBody.put("idToken", idToken);

    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    RequestBody body = RequestBody.create(jsonBody.toString(), JSON);

    String url = Constants.ACCOUNT_LOOKUP_URL + "?key=" + Constants.FIREBASE_AUTH_ID;

    Request request = new Request.Builder().url(url).post(body).build();

    try {
      Response response = client.newCall(request).execute();
      String responseData = response.body().string();

      if (!response.isSuccessful()) {
        throw new RuntimeException("User lookup failed on our end, please try again.");
      }

      System.out.println("User lookup successful: " + responseData);
      JSONObject responseObject = new JSONObject(responseData);
      JSONObject userObject = responseObject.getJSONArray("users").getJSONObject(0);

      String uid = userObject.optString("localId");
      String email = userObject.optString("email");
      String displayName = userObject.optString("displayName", "No display name");
      long createdAt = Long.parseLong(userObject.optString("createdAt"));
      LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(createdAt), ZoneId.systemDefault());

      CommonUserFactory userFactory = new CommonUserFactory();
      return userFactory.create(uid, email, displayName, password, dateTime);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException("Unable to connect to our backend, please try again.");
    }
  }

  @Override
  public boolean existsByName(String identifier) {
    return false;
  } // TODO: Ensure this successfully searches by email

  @Override
  public Optional<String> save(User user) {
    JSONObject jsonBody =
        new JSONObject().put("email", user.getEmail()).put("password", user.getPassword());
    // jsonMap.put("displayName", user.getName());  // For Firebase default field to store username
    System.out.println(jsonBody.toString()); // TODO: Get rid of this

    HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.SIGNUP_URL).newBuilder();
    urlBuilder.addQueryParameter("key", Constants.FIREBASE_AUTH_ID);

    RequestBody body = RequestBody.create(jsonBody.toString(), MediaType.parse("application/json"));

    Request request = new Request.Builder().url(urlBuilder.build().toString()).post(body).build();
    try {
      Response response = client.newCall(request).execute();
      if (response.code() == 200) {
        // Success
        return Optional.empty();
      } else {
        JSONObject jsonResponse = new JSONObject(response.body().string());
        System.out.println("Response body " + jsonResponse);
        JSONArray errors = jsonResponse.getJSONObject("error").getJSONArray("errors");
        String firstErrorMessage = errors.getJSONObject(0).getString("message");
        return Optional.of(firstErrorMessage);
      }
    } catch (IOException | JSONException e) {
      return Optional.of("Unexpected error signing up. Please try again.");
    }
  }

  @Override
  public User get() {
    return user;
  }
}
