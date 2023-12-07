package data_access;

import com.google.gson.Gson;
import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import okhttp3.*;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.rooms.RoomsDataAccessInterface;
import use_case.settings.RoomsSettingsDataAccessInterface;

public class FirebaseRoomsDataAccessObject
    implements RoomsDataAccessInterface, RoomsSettingsDataAccessInterface {


  final class MessageContentsJSON {
    String author;
    String contents;
  }

  class CustomIterator implements Iterator<Message> {
    private final List<String> timestamps;
    private final Map<String, MessageContentsJSON> messages;
    private int index = 0;

    CustomIterator(Map<String, MessageContentsJSON> messages) {
      if (messages == null) {
        timestamps = new ArrayList<>();
        this.messages = new HashMap<>();
      } else {
        timestamps = new ArrayList<>(messages.keySet());
        this.messages = messages;
      }
    }

    // Checks if the next element exists
    public boolean hasNext() {
      return index < timestamps.size();
    }

    // moves the cursor/iterator to next element
    public Message next() {
      String timestamp = timestamps.get(index);
      index += 1;
      Instant timestampInstant = Instant.ofEpochMilli(Long.parseLong(timestamp));
      String author = messages.get(timestamp).author;
      String contents = messages.get(timestamp).contents;
      return new Message(timestampInstant, contents, new DisplayUser(author, author));
    }
  }

  class MessageJSON implements Iterable<Message> {
    String name;
    Map<String, MessageContentsJSON> messages;
    Map<String, String> users;

    public Iterator<Message> iterator() {
      return new CustomIterator(messages);
    }
  }

  private final OkHttpClient client;

  public FirebaseRoomsDataAccessObject(OkHttpClient client) {
    this.client = client;
  }

  private void changeRoomDisplayName(User user, String roomId) {}

  public void propogateDisplayNameChange(String idToken, User user) {
    List<String> availableRoomIds = getAvailableRoomIds(user);
    for (String roomId : availableRoomIds) {
      addUserToRoomData(user.toDisplayUser(), idToken, roomId);
    }
  }

  @Override
  public List<String> getAvailableRoomIds(User user) {
    String encodedEmail =
        Base64.getEncoder().encodeToString(user.getEmail().toLowerCase().getBytes());
    String url = String.format(Constants.ROOM_DATA_URL, encodedEmail);
    Request request = new Request.Builder().url(url).get().build();

    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        throw new IOException();
      }
      List<String> roomIds = new ArrayList<>();
      String responseString = response.body().string();
      if (!responseString.equals("null")) {
        JSONObject roomResponse = new JSONObject(responseString);
        roomResponse.keys().forEachRemaining(roomIds::add);
      }
      return roomIds;
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to get available rooms. Please try again.");
    }
  }

  @Override
  public Room getRoomFromId(String idToken, User user, String roomId) {
    String url = String.format(Constants.ROOM_URL, roomId) + "?auth=" + idToken;
    Request request = new Request.Builder().url(url).get().build();

    try {
      Response response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        // Fetch JSON from response
        String jsonText = response.body().string();



        // Use GSON library to cast JSON into our RoomJSON object
        MessageJSON roomMessages = new Gson().fromJson(jsonText, MessageJSON.class);
        // Get iterator from our MessageJSON iterable
        Iterator<Message> roomMessageIterator = roomMessages.iterator();

        List<Message> messages = new ArrayList<>();
        // Load all messages into a new list
        while(roomMessageIterator.hasNext()) {
          messages.add(roomMessageIterator.next());
        }


        messages.sort(Comparator.comparing(a -> a.timestamp));

        // Get DisplayUsers
        JSONObject rooms = new JSONObject(jsonText);
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

        return new Room(roomId, roomName, displayUsers, messages);
      } else {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to retrieve user data. Please try again.");
    }
  }

  private void addRoomToUserData(DisplayUser newUser, String idToken, Room room) {
    String jsonBody = "true";
    String encodedEmail =
        Base64.getEncoder().encodeToString(newUser.getEmail().toLowerCase().getBytes());
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
  public void removeUserFromRoom(
      String idToken, User currentUser, DisplayUser userToRemove, Room room) {
    String encodedEmail =
        Base64.getEncoder().encodeToString(userToRemove.getEmail().toLowerCase().getBytes());
    String url =
        String.format(Constants.ROOM_USERS_URL, room.getUid(), encodedEmail) + "?auth=" + idToken;
    Request request = new Request.Builder().url(url).delete().build();

    try {
      Response response = client.newCall(request).execute();
      if (response.isSuccessful()) {
        deleteRoomFromUserData(userToRemove, room, idToken);
      } else {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to delete user from room. Please try again.");
    }
  }

  private void addUserToRoomData(DisplayUser newUser, String idToken, String roomId) {
    String jsonBody = JSONObject.quote(newUser.getName());
    String encodedEmail =
        Base64.getEncoder().encodeToString(newUser.getEmail().toLowerCase().getBytes());
    String url = String.format(Constants.ROOM_USERS_URL, roomId, encodedEmail) + "?auth=" + idToken;
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
  public void addUserToRoom(String idToken, User currentUser, DisplayUser newUser, Room room) {
    addUserToRoomData(newUser, idToken, room.getUid());
    addRoomToUserData(newUser, idToken, room);
  }

  @Override
  public Room addRoom(String idToken, User user, String roomName) {
    JSONObject roomJSON = new JSONObject();
    // Add room name
    roomJSON.put("name", roomName);
    // Add room users
    JSONObject roomUsersJSON = new JSONObject();
    String encodedEmail =
        Base64.getEncoder().encodeToString(user.getEmail().toLowerCase().getBytes());
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
    String encodedEmail =
        Base64.getEncoder().encodeToString(user.getEmail().toLowerCase().getBytes());
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
  public void deleteRoom(String idToken, User user, Room room) {
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

  @Override
  public void changeRoomName(String idToken, User user, Room activeRoom, String roomName) {
    String jsonBody = JSONObject.quote(roomName);
    String url = String.format(Constants.ROOM_NAME_URL, activeRoom.getUid()) + "?auth=" + idToken;
    RequestBody body = RequestBody.create(jsonBody, MediaType.parse("application/json"));
    Request request = new Request.Builder().url(url).put(body).build();
    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        throw new IOException();
      }
    } catch (IOException | JSONException e) {
      throw new RuntimeException("Unable to change room name. Please try again.");
    }
  }
}
