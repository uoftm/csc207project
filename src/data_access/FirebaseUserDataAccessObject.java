package data_access;

import entities.auth.DisplayUser;
import entities.auth.User;
import entities.auth.UserFactory;
import entities.rooms.Message;
import entities.rooms.Room;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Iterator;
import java.util.List;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.chat.ChatUserDataAccessInterface;
import use_case.login.LoginUserDataAccessInterface;
import use_case.settings.DeleteUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class FirebaseUserDataAccessObject
    implements SignupUserDataAccessInterface,
        LoginUserDataAccessInterface,
        DeleteUserDataAccessInterface,
        ChatUserDataAccessInterface {

  private final OkHttpClient client;

  private String token = null;

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

  private String getAccessToken(String email, String password) {
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
  public User get(String email, String password) {
    token = getAccessToken(email, password);
    // Initialize user from our idToken and password by making a second call to Firebase
    return getUserData(token, password);
  }

  public User get() {
    return getUserData(token, "");
  }

  public String getDisplayName(String uid) {
    // Note: This method doesn't require authentication, as anyone is allowed to retrieve a display
    // name given an uid
    String url = String.format(Constants.DISPLAY_NAME_URL, uid);
    Request request = new Request.Builder().url(url).get().build();

    try {
      Response response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        return response.body().string().replace('"', ' ').trim();
      } else {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to retrieve display name. Please try again.");
    }
  }

  // Return username, time user was created, and uid
  private User getUserData(String idToken, String password) {
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
      String displayName = getDisplayName(uid);
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

  @Override
  public boolean existsByName(String identifier) {
    return false;
  } // TODO: Ensure this successfully searches by email

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

  private void saveDisplayName(String uid, String displayName, String idToken) {
    String jsonBody = JSONObject.quote(displayName);
    String url = String.format(Constants.DISPLAY_NAME_URL, uid) + "?auth=" + idToken;
    RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

    Request request = new Request.Builder().url(url).put(body).build();
    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        throw new RuntimeException("Unable to save display name. Please try again.");
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to save display name. Please try again.");
    }
  }

  @Override
  public void save(User user) {
    SignupResults signupResults = signup(user);
    saveDisplayName(signupResults.uid, user.getName(), signupResults.idToken);
  }

  @Override
  public List<Room> getAvailableRooms(User user) {
    List<String> roomUids = getAvailableRoomUids(user);
    List<Room> availableRooms = new ArrayList<>();

    for (String roomUid : roomUids) {
      System.out.println(roomUid);
      try {
        Room room = getRoomByUid(user, roomUid);
        availableRooms.add(room);
      } catch (RuntimeException e) {
        System.out.println("Error fetching room: " + roomUid + ". Error: " + e.getMessage());
      }
    }

    return availableRooms;
  }

  private List<String> getAvailableRoomUids(User user) {
    String encodedEmail =
            Base64.getEncoder().encodeToString(user.getEmail().toLowerCase().getBytes());
    String url = String.format(Constants.ROOM_DATA_URL, encodedEmail);
    Request request = new Request.Builder().url(url).get().build();

    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        return new ArrayList<>();
      }
      System.out.println(response);
      JSONObject roomResponse = new JSONObject(response.body().string());
      List<String> roomUids = new ArrayList<>();
      roomResponse.keys().forEachRemaining(roomUids::add);
      System.out.println(roomResponse);
      return roomUids;
    } catch (IOException | JSONException e) {
      return new ArrayList<>();
    }
  }

  private Room getRoomByUid(User user, String roomId) {
    String token = getAccessToken(user.getEmail(), user.getPassword());

    String url = String.format(Constants.ROOM_URL, roomId) + "?auth=" + token;
    Request request = new Request.Builder().url(url).get().build();

    try {
      Response response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        JSONObject rooms = new JSONObject(response.body().string());

        // Get messages
        List<Message> messages = new ArrayList<>();
        if (rooms.has("messages")) {
          JSONObject messagesJSON = rooms.getJSONObject("messages");
          Iterator<String> timestampIterator = messagesJSON.keys();
          while (timestampIterator.hasNext()) {
            String key = timestampIterator.next();
            Instant timestamp = Instant.ofEpochSecond(Long.parseLong(key));
            String contents = messagesJSON.getJSONObject(key).getString("contents");
            String encodedAuthorEmail = messagesJSON.getJSONObject(key).getString("author");
            String authorEmail = new String(Base64.getDecoder().decode(encodedAuthorEmail));
            Message message = new Message(timestamp, contents, authorEmail);
            messages.add(message);
          }
        }

        // Get DisplayUsers
        List<DisplayUser> displayUsers = new ArrayList<>();
        if (rooms.has("users")) {
          JSONObject usersJSON = rooms.getJSONObject("users");
          Iterator<String> encodedEmailIterator = usersJSON.keys();
          while (encodedEmailIterator.hasNext()) {
            String encodedEmail = encodedEmailIterator.next();
            String userEmail = new String(Base64.getDecoder().decode(encodedEmail));
            String displayName = usersJSON.getString(encodedEmail);
            DisplayUser displayUser = new DisplayUser(userEmail, displayName);
            displayUsers.add(displayUser);
          }
        }

        // Get name
        String roomName = rooms.getString("name");

        Room room = new Room(roomId, roomName, displayUsers, messages);
        return room;
      } else {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to retrieve user data. Please try again.");
    }
  }

  private void deleteFirebaseUserData(User user, String idToken) {
    String url = String.format(Constants.USER_DATA_URL, user.getUid()) + "?auth=" + idToken;
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
    // Note: this doesn't remove the user from any rooms for which they may be a member
    // Before calling this method, please remove the user from their rooms using the Rooms DAO
    String idToken = getAccessToken(user.getEmail(), user.getPassword());
    deleteFirebaseUserData(user, idToken);
    deleteUserFromAuth(user);
  }
}
