package data_access;

import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Message;

import java.io.IOException;
import java.time.Instant;
import java.util.*;

import entities.rooms.Room;
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
  public Room getRoomFromId(User user, LoginUserDataAccessInterface userDAO, String roomId) {
    String idToken = userDAO.getAccessToken(user.getEmail(), user.getPassword());

    String url = String.format(Constants.ROOM_DATA_URL, roomId) + "?auth=" + idToken;
    Request request = new Request.Builder().url(url).get().build();

    try {
      Response response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        JSONObject rooms = new JSONObject(response.body().string());

        // Get messages
        JSONObject messagesJSON = rooms.getJSONObject("messages");
        List<Message> messages = new ArrayList<>();
        for (String key: new ArrayList<String>((Collection) messagesJSON.keys())) {
          Instant timestamp = Instant.ofEpochSecond(Long.parseLong(key));
          String contents = messagesJSON.getJSONObject(key).getString("contents");
          String authorEmail = messagesJSON.getJSONObject(key).getString("author");
          Message message = new Message(timestamp, contents, authorEmail);
          messages.add(message);
        }

        // Get DisplayUsers
        JSONObject usersJSON = rooms.getJSONObject("users");
        List<DisplayUser> displayUsers = new ArrayList<>();
        for (String userEmail: new ArrayList<String>((Collection) usersJSON.keys())) {
          String displayName = usersJSON.getString(userEmail);
          DisplayUser displayUser = new DisplayUser(userEmail, displayName);
          displayUsers.add(displayUser);
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

  private void addRoomToUserData(User user, String idToken, Room room) {
    String jsonBody = JSONObject.quote("true");
    String url = String.format(Constants.SPECIFIC_ROOM_DATA_URL, user.getEmail(), room.getUid()) + "?auth=" + idToken;
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
  public Room addRoom(User user, LoginUserDataAccessInterface userDAO, String roomName) {
    String idToken = userDAO.getAccessToken(user.getEmail(), user.getPassword());

    JSONObject roomJSON = new JSONObject();
    // Add room name
    roomJSON.put("name", roomName);
    // Add room users
    JSONObject roomUsersJSON = new JSONObject();
    roomUsersJSON.put(user.getEmail(), user.getName());

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
        userList.add(new DisplayUser(user.getEmail(), user.getName()));
        Room room = new Room(roomId, roomName, userList, new ArrayList<>());
        addRoomToUserData(user, idToken, room);
        return room;
      } else {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to create room. Please try again.");
    }
  }

  private void deleteRoomFromUserData(DisplayUser user, Room room, String idToken) {
    String url = String.format(Constants.SPECIFIC_ROOM_DATA_URL, user.getEmail(), room.getUid()) + "?auth=" + idToken;
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

    for (DisplayUser roomUser: room.getUsers()) {
      deleteRoomFromUserData(roomUser, room, idToken);
    }

    String url = String.format(Constants.ROOM_DATA_URL, room.getUid()) + "?auth=" + idToken;
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
