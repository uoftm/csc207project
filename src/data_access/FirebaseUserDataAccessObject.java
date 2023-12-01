package data_access;

import entity.*;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
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
  class RawUserData {
    String uid;
    String displayName;
    long createdAt;

    RawUserData(String uid, String displayName, long createdAt) {
      this.uid = uid;
      this.displayName = displayName;
      this.createdAt = createdAt;
    }
  }

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

      if (!authResponse.isSuccessful()) {
        System.out.println("Authentication failed: " + authResponseData);
        return null;
      }

      JSONObject authResponseJson = new JSONObject(authResponseData);

      if (!authResponseJson.has("idToken") || !authResponseJson.has("localId")) {
        System.out.println("Invalid authentication response");
        return null;
      }

      String idToken = authResponseJson.getString("idToken");

      var userData = getUserData(idToken);
      if (userData == null) {
        return null;
      }

      String displayName = userData.displayName;
      long createdAt = userData.createdAt;
      Instant instant = Instant.ofEpochMilli(createdAt);
      LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

      System.out.println("Display Name: " + displayName);
      System.out.println("Created At: " + dateTime);

      CommonUserFactory userFactory = new CommonUserFactory();
      user = userFactory.create(userData.uid, email, displayName, password, dateTime);
      return user;
    } catch (IOException | JSONException e) {
      System.out.println("Error during authentication: " + e.getMessage());
      return null;
    }
  }

  // Return username, time user was created
  private RawUserData getUserData(String idToken) {
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
        throw new Exception("User lookup failed: " + responseData);
      }

      System.out.println("User lookup successful: " + responseData);
      JSONObject responseObject = new JSONObject(responseData);
      JSONObject userObject = responseObject.getJSONArray("users").getJSONObject(0);

      String uid = userObject.optString("localId");
      String displayName = userObject.optString("displayName", "No display name");
      String createdAt =
          userObject.optString(
              "createdAt", Instant.now().toEpochMilli() + "L"); // TODO: remove this placeholder

      return new RawUserData(uid, displayName, Long.parseLong(createdAt));
    } catch (Exception e) {
      e.printStackTrace();
      return null;
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

  // TODO: Remove dummy code and connect to firebase
  @Override
  public List<Room> getAvailableRooms(User user) {
    DisplayUser dummy_display_user = new DisplayUser("foo", "bar");
    List<DisplayUser> users = new ArrayList<>();
    users.add(dummy_display_user);

    Instant timestamp = Instant.now();
    Message message = new Message(timestamp, "This is a test message.", dummy_display_user.getUid());

    List<Message> messages = new ArrayList<>();
    messages.add(message);

    Room dummy_room = new Room("", "baz", users, messages);
    List<Room> availableRooms = new ArrayList<>();
    availableRooms.add(dummy_room);

    return availableRooms;
  }
}
