package data_access;

import entity.CommonUserFactory;
import entity.User;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class FirebaseUserDataAccessObject
    implements SignupUserDataAccessInterface, LoginUserDataAccessInterface {
  static OkHttpClient client = new OkHttpClient();

  @Override
  public Optional<User> get(String email, String password) {
    // Authentication Request
    JSONObject jsonBody = new JSONObject();
    jsonBody.put("email", email);
    jsonBody.put("password", password);
    jsonBody.put("returnSecureToken", true);

    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    RequestBody body = RequestBody.create(JSON, jsonBody.toString());

    String authUrl =
        "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyBuJk14Gljk-chdN_9YVywSKnf38ttwUVg";

    Request authRequest = new Request.Builder().url(authUrl).post(body).build();

    try {
      Response authResponse = client.newCall(authRequest).execute();
      String authResponseData = authResponse.body().string();

      if (!authResponse.isSuccessful()) {
        System.out.println("Authentication failed: " + authResponseData);
        return Optional.empty();
      }

      JSONObject authResponseJson = new JSONObject(authResponseData);

      if (!authResponseJson.has("idToken") || !authResponseJson.has("localId")) {
        System.out.println("Invalid authentication response");
        return Optional.empty();
      }

      String idToken = authResponseJson.getString("idToken");

      return getUserData(idToken)
          .map(
              userData -> {
                String displayName = userData[0];
                long createdAt = Long.parseLong(userData[1]);
                Instant instant = Instant.ofEpochMilli(createdAt);
                LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

                System.out.println("Display Name: " + displayName);
                System.out.println("Created At: " + dateTime);

                CommonUserFactory userFactory = new CommonUserFactory();
                return userFactory.create(email, displayName, password, dateTime);
              });
    } catch (IOException | JSONException e) {
      System.out.println("Error during authentication: " + e.getMessage());
      return Optional.empty();
    }
  }

  // Return username, time user was created
  private Optional<String[]> getUserData(String idToken) {
    JSONObject jsonBody = new JSONObject();
    jsonBody.put("idToken", idToken);

    MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    RequestBody body = RequestBody.create(JSON, jsonBody.toString());

    String url =
        "https://identitytoolkit.googleapis.com/v1/accounts:lookup?key=AIzaSyBuJk14Gljk-chdN_9YVywSKnf38ttwUVg";

    Request request = new Request.Builder().url(url).post(body).build();

    try {
      Response response = client.newCall(request).execute();
      String responseData = response.body().string();

      if (response.isSuccessful()) {
        System.out.println("User lookup successful: " + responseData);
        JSONObject responseObject = new JSONObject(responseData);
        JSONObject userObject = responseObject.getJSONArray("users").getJSONObject(0);

        String displayName = userObject.optString("displayName", "No display name");
        String createdAt =
            userObject.optString("createdAt", "1701105347550L"); // TODO: remove this placeholder

        return Optional.ofNullable(new String[] {displayName, createdAt});
      } else {
        System.out.println("User lookup failed: " + responseData);
        return Optional.empty();
      }
    } catch (IOException | JSONException e) {
      e.printStackTrace();
      return Optional.empty();
    }
  }

  @Override
  public boolean existsByName(String identifier) {
    return false;
  } // TODO: Ensure this successfully searches by email

  @Override
  public Optional<String> save(User user) {
    Map<String, String> jsonMap = new HashMap<>();
    jsonMap.put("email", user.getEmail());
    // jsonMap.put("displayName", user.getName());  // For Firebase default field to store username
    jsonMap.put("password", user.getPassword());
    JSONObject jsonBody = new JSONObject(jsonMap);
    System.out.println(jsonBody.toString()); // TODO: Get rid of this

    HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.SIGNUP_URL).newBuilder();
    urlBuilder.addQueryParameter("key", Constants.FIREBASE_AUTH_ID);

    RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody.toString());

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
}
