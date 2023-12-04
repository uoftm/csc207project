package rooms;

import static org.junit.Assert.*;

import data_access.FirebaseMessageDataAccessObject;
import data_access.FirebaseRoomsDataAccessObject;
import data_access.FirebaseUserDataAccessObject;
import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import okhttp3.OkHttpClient;
import org.junit.Test;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.MessageDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;

public class FirebaseRoomsDataAccessObjectTest {

  // TODO: Make tests pass by creating real objects in Firebase and deleting them afterwards

  @Test
  public void testLoadMessagesSuccess() {
    OkHttpClient client = new OkHttpClient();
    RoomsDataAccessInterface dao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    Room dummyRoom = createDummyRoom();
    User dummyUser = createDummyUser();
    List<Message> response =
        dao.getRoomFromId(dummyUser, userDao, dummyRoom.getUid()).getMessages();
    assertNotNull(response);
  }

  @Test
  public void testLoadMessagesFailure() {
    RoomsDataAccessInterface dao =
        new FirebaseRoomsDataAccessObject(null) {
          @Override
          public Room getRoomFromId(
              User user, LoginUserDataAccessInterface userDao, String roomId) {
            throw new RuntimeException("Failed to retrieve messages.");
          }
        };
    Room dummyRoom = createDummyRoom();
    User dummyUser = createDummyUser();
    assertThrows(
        "Failed to retrieve messages.",
        RuntimeException.class,
        () -> dao.getRoomFromId(dummyUser, null, dummyRoom.getUid()).getMessages());
  }

  @Test
  public void testSendMessageSuccess() {
    OkHttpClient client = new OkHttpClient();
    MessageDataAccessInterface dao = new FirebaseMessageDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    Room dummyRoom = createDummyRoom();
    User dummyUser = createDummyUser();
    Message message = createDummyMessage();
    dao.sendMessage(dummyRoom, userDao, dummyUser, message);
  }

  @Test
  public void testSendMessageFailure() {
    MessageDataAccessInterface dao =
        new FirebaseMessageDataAccessObject(null) {
          @Override
          public void sendMessage(
              Room room, LoginUserDataAccessInterface userDao, User user, Message message) {
            throw new RuntimeException("Failed to send message.");
          }
        };
    Room dummyRoom = createDummyRoom();
    User dummyUser = createDummyUser();
    Message dummyMessage = createDummyMessage();
    assertThrows(
        "Failed to send message.",
        RuntimeException.class,
        () -> dao.sendMessage(dummyRoom, null, dummyUser, dummyMessage));
  }

  @Test
  public void testAddUserToRoomSuccess() {
    OkHttpClient client = new OkHttpClient();
    FirebaseRoomsDataAccessObject dao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    Room dummyRoom = createDummyRoom();
    User dummyUser = createDummyUser();
    DisplayUser dummyDisplayUser = createDummyDisplayUser();

    dao.addUserToRoom(dummyUser, dummyDisplayUser, userDao, dummyRoom);
  }

  @Test
  public void testAddUserToRoomFailure() {
    RoomsDataAccessInterface dao =
        new FirebaseRoomsDataAccessObject(null) {
          @Override
          public void addUserToRoom(
              User user, DisplayUser displayUser, LoginUserDataAccessInterface userDao, Room room) {
            throw new RuntimeException("Failed to add user.");
          }
        };
    Room dummyRoom = createDummyRoom();
    User dummyUser = createDummyUser();
    DisplayUser dummyDisplayUser = createDummyDisplayUser();
    assertThrows(
        "Failed to add user.",
        RuntimeException.class,
        () -> dao.addUserToRoom(dummyUser, dummyDisplayUser, null, dummyRoom));
  }

  @Test
  public void testCreateRoomSuccess() {
    OkHttpClient client = new OkHttpClient();
    RoomsDataAccessInterface dao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    User dummyUser = createDummyUser();
    String roomName = "New Room";
    assertNotNull(dao.addRoom(dummyUser, userDao, roomName));
  }

  @Test
  public void testCreateRoomFailure() {
    RoomsDataAccessInterface dao =
        new FirebaseRoomsDataAccessObject(null) {
          @Override
          public Room addRoom(User user, LoginUserDataAccessInterface userDao, String roomName) {
            throw new RuntimeException("Failed to create room.");
          }
        };
    User dummyUser = createDummyUser();
    String roomName = "New Room";
    assertThrows(
        "Failed to create room.",
        RuntimeException.class,
        () -> dao.addRoom(dummyUser, null, roomName));
  }

  private User createDummyUser() {
    return new User("dummyUid", "dummy@example.com", "Dummy User", "password", LocalDateTime.now());
  }

  private DisplayUser createDummyDisplayUser() {
    return new DisplayUser("dummyUid", "dummy@example.com");
  }

  private Room createDummyRoom() {
    DisplayUser dummyDisplayUser = new DisplayUser("dummyUid", "Dummy User");
    List<DisplayUser> users = new ArrayList<>();
    users.add(dummyDisplayUser);
    List<Message> messages = new ArrayList<>();
    messages.add(new Message(Instant.now(), "Dummy message", dummyDisplayUser.getEmail()));
    return new Room("dummyRoomUid", "Dummy Room", users, messages);
  }

  private Message createDummyMessage() {
    return new Message(Instant.now(), "Dummy message", "dummy@example.com");
  }
}
