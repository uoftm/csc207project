package data_access;

import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;

public class FirebaseRoomsDataAccessObject implements RoomsDataAccessInterface {

  private final OkHttpClient client;

  public FirebaseRoomsDataAccessObject(OkHttpClient client) {
    this.client = client;
  }

  @Override
  public List<String> getAvailableRoomIds(User user) {
    String encodedEmail = Base64.getEncoder().encodeToString(user.getEmail().toLowerCase().getBytes());
    String url = String.format(Constants.ROOM_DATA_URL, encodedEmail);
    Request request = new Request.Builder().url(url).get().build();

    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        throw new IOException();
      }
      JSONObject roomResponse = new JSONObject(response.body().string());
      return new ArrayList<>((Collection) roomResponse.keys());
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to save display name. Please try again.");
    }
  }

  @Override
  public Room getRoomFromId(User user, LoginUserDataAccessInterface userDAO, String roomId) {
    String idToken = userDAO.getAccessToken(user.getEmail(), user.getPassword());

    String url = String.format(Constants.ROOM_URL, roomId) + "?auth=" + idToken;
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

  private void addRoomToUserData(DisplayUser newUser, String idToken, Room room) {
    String jsonBody = JSONObject.quote("true");
    String encodedEmail = Base64.getEncoder().encodeToString(newUser.getEmail().toLowerCase().getBytes());
    String url =
        String.format(Constants.SPECIFIC_ROOM_DATA_URL, encodedEmail, room.getUid())
            + "?auth="
            + idToken;
    RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));
    Request request = new Request.Builder().url(url).put(body).build();

    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to add user to room. Please try again.");
    }
  }

  @Override
  public void addUserToRoom(User currentUser, DisplayUser newUser, LoginUserDataAccessInterface userDAO, Room room) {
    String idToken = userDAO.getAccessToken(currentUser.getEmail(), currentUser.getPassword());

    String jsonBody = "true";
    String encodedEmail = Base64.getEncoder().encodeToString(newUser.getEmail().toLowerCase().getBytes());
    String url = String.format(Constants.ROOM_URL, room.getUid(), encodedEmail) + "?auth=" + idToken;
    RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

    Request request = new Request.Builder().url(url).put(body).build();

    try {
      Response response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        addRoomToUserData(newUser, idToken, room);
      } else {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to add user to room. Please try again.");
    }
  }

  @Override
  public Room addRoom(User user, LoginUserDataAccessInterface userDAO, String roomName) {
    String idToken = userDAO.getAccessToken(user.getEmail(), user.getPassword());

    JSONObject roomJSON = new JSONObject();
    // Add room name
    roomJSON.put("name", roomName);
    // Add room users
    JSONObject roomUsersJSON = new JSONObject();
    String encodedEmail = Base64.getEncoder().encodeToString(user.getEmail().toLowerCase().getBytes());
    roomUsersJSON.put(encodedEmail, user.getName());
    roomJSON.put("users", roomUsersJSON);

    // Generate room id
    String roomId = UUID.randomUUID().toString();

    String jsonBody = roomJSON.toString();
    String url = String.format(Constants.ROOM_URL, roomId) + "?auth=" + idToken;
    RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));

    Request request = new Request.Builder().url(url).put(body).build();

    try {
      Response response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        List<DisplayUser> userList = new ArrayList<>();
        DisplayUser displayUser = new DisplayUser(user.getEmail(), user.getName());
        userList.add(displayUser);
        Room room = new Room(roomId, roomName, userList, new ArrayList<>());
        addRoomToUserData(displayUser, idToken, room);
        return room;
      } else {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to create room. Please try again.");
    }
  }

  private void deleteRoomFromUserData(DisplayUser user, Room room, String idToken) {
    String encodedEmail = Base64.getEncoder().encodeToString(user.getEmail().toLowerCase().getBytes());
    String url =
        String.format(Constants.SPECIFIC_ROOM_DATA_URL, encodedEmail, room.getUid())
            + "?auth="
            + idToken;
    Request request = new Request.Builder().url(url).delete().build();
    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to remove room from user data. Please try again.");
    }
  }

  @Override
  public void deleteRoom(User user, LoginUserDataAccessInterface userDAO, Room room) {
    String idToken = userDAO.getAccessToken(user.getEmail(), user.getPassword());

    for (DisplayUser roomUser : room.getUsers()) {
      deleteRoomFromUserData(roomUser, room, idToken);
    }

    String url = String.format(Constants.ROOM_URL, room.getUid()) + "?auth=" + idToken;
    Request request = new Request.Builder().url(url).delete().build();
    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to delete room. Please try again.");
    }
  }
}
