package data_access;

import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import use_case.rooms.Response;
import use_case.rooms.RoomsDataAccessInterface;

// TODO: You need to split this into multiple DAOs
public class FirebaseRoomsDataAccessObject implements RoomsDataAccessInterface {
  @Override
  public Response<List<Message>> loadMessages(Room room, User user) {
    // TODO: You should get the messages corresponding to room
    DisplayUser dummy_display_user = new DisplayUser("foo", "bar");
    Instant timestamp = Instant.now();
    Message message =
        new Message(timestamp, "This is a test message to load.", dummy_display_user.getUid());

    List<Message> messages = new ArrayList<>();
    messages.add(message);

    // Uncomment this to see what happens when you fail to get messages:
    // return new Response<>(null, "Failed to retrieve messages.");

    return new Response<>(messages, null);
  }

  @Override
  public Response<String> sendMessage(Room room, User user, String message) {
    // TODO: You should get the messages corresponding to room
    System.out.println(message);

    // Uncomment this to see what happens when you fail to send a message:
    // return new Response<>(null, "Failed to send message.");

    return new Response<>("success", null);
  }

  @Override
  public boolean validateRoomAccess(Room room, User user) {
    // TODO: You should validate that user is a member of room
    // This does not need to be of type Response
    return true;
  }

  @Override
  public Response<String> addUserToRoom(Room room, User user, String email) {
    // TODO: You should add "user" with email "email" to room "room"
    // user being a member of room has already been validated at this point

    // Uncomment this to see what happens when you fail to create room:
    // return new Response<>(null, "Failed to add user.");

    return new Response<>("User added successfully!", null);
  }

  @Override
  public Response<Room> createRoom(User user, String createRoom) {
    // TODO: You should create a room with members {user} and return this new room as a Room entity
    DisplayUser me = new DisplayUser(user.getUid(), user.getName());
    List<DisplayUser> users = new ArrayList<>();
    List<Message> messages = new ArrayList<>();
    users.add(me);

    // Uncomment this to see what happens when you fail to create room:
    // return new Response<Room>(null, "Failed to create room.");

    Room room = new Room("1234", createRoom, users, messages);
    return new Response<>(room, null);
  }
}
