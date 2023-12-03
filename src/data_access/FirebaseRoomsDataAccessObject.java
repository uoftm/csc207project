package data_access;

import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.rooms.RoomsResponse;
import use_case.rooms.RoomsDataAccessInterface;

// TODO: You need to split this into multiple DAOs
public class FirebaseRoomsDataAccessObject implements RoomsDataAccessInterface {
  private final OkHttpClient client;

  public FirebaseRoomsDataAccessObject(OkHttpClient client) {
    this.client = client;
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
  public RoomsResponse<List<Message>> loadMessages(Room room, User user) {
    // TODO: You should get the messages corresponding to room
    DisplayUser dummy_display_user = new DisplayUser("foo", "bar");
    Instant timestamp = Instant.now();
    Message message =
        new Message(timestamp, "This is a test message to load.", dummy_display_user.getUid());

    List<Message> messages = new ArrayList<>();
    messages.add(message);

    // Uncomment this to see what happens when you fail to get messages:
    // Response<List<Messages>> response = new Response<List<Messages>>(null);
    // response.setError("Failed to retrieve messages.");
    // return response;

    return new RoomsResponse<>(messages);
  }

  @Override
  public RoomsResponse<String> sendMessage(Room room, User user, String message) {
    // TODO: You should get the messages corresponding to room
    System.out.println(message);

    // Uncomment this to see what happens when you fail to send a message:
    // Response<String> response = new Response<String>(null);
    // response.setError("Failed to send message.");
    // return response;

    return new RoomsResponse<>("success");
  }

  @Override
  public boolean validateRoomAccess(Room room, User user) {
    // TODO: You should validate that user is a member of room
    // This does not need to be of type Response
    return true;
  }

  @Override
  public RoomsResponse<String> addUserToRoom(Room room, User user, String email) {
    // TODO: You should add "user" with email "email" to room "room"
    // user being a member of room has already been validated at this point

    // Uncomment this to see what happens when you fail to create room:
    // Response<String> response = new Response<String>(null);
    // response.setError("Failed to add user.");
    // return response;

    return new RoomsResponse<>("User added successfully!");
  }

  @Override
  public RoomsResponse<Room> createRoom(User user, String roomToCreateName) {
    // Get user access token
    String token = getAccessToken(user.getEmail(), user.getPassword());

    // Create room JSON Object with name and users
    JSONObject roomJSON = new JSONObject();
    roomJSON.put("name", roomToCreateName);
    JSONObject roomUsersJSON = new JSONObject();
    String encodedEmail =
            Base64.getEncoder().encodeToString(user.getEmail().toLowerCase().getBytes());
    roomUsersJSON.put(encodedEmail, user.getName());
    roomJSON.put("users", roomUsersJSON);

    // Generate a new room UID and add it to JSON data, and format JSON body
    String roomUid = UUID.randomUUID().toString();
    String jsonBody = roomJSON.toString();
    String url = String.format(Constants.ROOM_URL, roomUid) + "?auth=" + token;
    RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

    Request request = new Request.Builder().url(url).put(body).build();

    try {
      Response response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        System.out.println(response.message());
        List<DisplayUser> users = new ArrayList<>();
        users.add(new DisplayUser(user.getUid(), user.getName()));
        Room room = new Room(roomUid, roomToCreateName, users, new ArrayList<>());
        return new RoomsResponse<>(room);
      } else {
        RoomsResponse<Room> roomsResponse = new RoomsResponse<>(null);
        roomsResponse.setError("Request Denied.");
        return roomsResponse;
      }
    } catch (IOException | JSONException e) {
      RoomsResponse<Room> roomsResponse = new RoomsResponse<>(null);
      roomsResponse.setError("Unable to create room.");
      return roomsResponse;
    }
  }
}
