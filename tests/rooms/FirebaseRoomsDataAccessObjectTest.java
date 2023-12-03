package rooms;

import static org.junit.Assert.*;

import data_access.FirebaseRoomsDataAccessObject;
import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import use_case.rooms.RoomsResponse;

public class FirebaseRoomsDataAccessObjectTest {

  @Test
  public void testLoadMessagesSuccess() {
    FirebaseRoomsDataAccessObject dao = new FirebaseRoomsDataAccessObject();
    Room dummyRoom = createDummyRoom();
    User dummyUser = createDummyUser();
    RoomsResponse<List<Message>> response = dao.loadMessages(dummyRoom, dummyUser);
    assertFalse(response.isError());
    assertNotNull(response.getVal());
  }

  @Test
  public void testLoadMessagesFailure() {
    FirebaseRoomsDataAccessObject dao =
        new FirebaseRoomsDataAccessObject() {
          @Override
          public RoomsResponse<List<Message>> loadMessages(Room room, User user) {
            RoomsResponse<List<Message>> response = new RoomsResponse<>(null);
            response.setError("Failed to retrieve messages.");
            return response;
          }
        };
    Room dummyRoom = createDummyRoom();
    User dummyUser = createDummyUser();
    RoomsResponse<List<Message>> response = dao.loadMessages(dummyRoom, dummyUser);
    assertTrue(response.isError());
    assertEquals("Failed to retrieve messages.", response.getError());
  }

  @Test
  public void testSendMessageSuccess() {
    FirebaseRoomsDataAccessObject dao = new FirebaseRoomsDataAccessObject();
    Room dummyRoom = createDummyRoom();
    User dummyUser = createDummyUser();
    String message = "Test message";
    RoomsResponse<String> response = dao.sendMessage(dummyRoom, dummyUser, message);
    assertFalse(response.isError());
    assertEquals("success", response.getVal());
  }

  @Test
  public void testSendMessageFailure() {
    FirebaseRoomsDataAccessObject dao =
        new FirebaseRoomsDataAccessObject() {
          @Override
          public RoomsResponse<String> sendMessage(Room room, User user, String message) {
            RoomsResponse<String> response = new RoomsResponse<>(null);
            response.setError("Failed to send message.");
            return response;
          }
        };
    Room dummyRoom = createDummyRoom();
    User dummyUser = createDummyUser();
    String message = "Test message";
    RoomsResponse<String> response = dao.sendMessage(dummyRoom, dummyUser, message);
    assertTrue(response.isError());
    assertEquals("Failed to send message.", response.getError());
  }

  @Test
  public void testValidateRoomAccess() {
    FirebaseRoomsDataAccessObject dao = new FirebaseRoomsDataAccessObject();
    Room dummyRoom = createDummyRoom();
    User dummyUser = createDummyUser();
    boolean result = dao.validateRoomAccess(dummyRoom, dummyUser);
    assertTrue(result);
  }

  @Test
  public void testAddUserToRoomSuccess() {
    FirebaseRoomsDataAccessObject dao = new FirebaseRoomsDataAccessObject();
    Room dummyRoom = createDummyRoom();
    User dummyUser = createDummyUser();
    String email = "test@example.com";
    RoomsResponse<String> response = dao.addUserToRoom(dummyRoom, dummyUser, email);
    assertFalse(response.isError());
    assertEquals("User added successfully!", response.getVal());
  }

  @Test
  public void testAddUserToRoomFailure() {
    FirebaseRoomsDataAccessObject dao =
        new FirebaseRoomsDataAccessObject() {
          @Override
          public RoomsResponse<String> addUserToRoom(Room room, User user, String email) {
            RoomsResponse<String> response = new RoomsResponse<>(null);
            response.setError("Failed to add user.");
            return response;
          }
        };
    Room dummyRoom = createDummyRoom();
    User dummyUser = createDummyUser();
    String email = "test@example.com";
    RoomsResponse<String> response = dao.addUserToRoom(dummyRoom, dummyUser, email);
    assertTrue(response.isError());
    assertEquals("Failed to add user.", response.getError());
  }

  @Test
  public void testCreateRoomSuccess() {
    FirebaseRoomsDataAccessObject dao = new FirebaseRoomsDataAccessObject();
    User dummyUser = createDummyUser();
    String roomName = "New Room";
    RoomsResponse<Room> response = dao.createRoom(dummyUser, roomName);
    assertFalse(response.isError());
    assertNotNull(response.getVal());
  }

  @Test
  public void testCreateRoomFailure() {
    FirebaseRoomsDataAccessObject dao =
        new FirebaseRoomsDataAccessObject() {
          @Override
          public RoomsResponse<Room> createRoom(User user, String roomName) {
            RoomsResponse<Room> response = new RoomsResponse<>(null);
            response.setError("Failed to create room.");
            return response;
          }
        };
    User dummyUser = createDummyUser();
    String roomName = "New Room";
    RoomsResponse<Room> response = dao.createRoom(dummyUser, roomName);
    assertTrue(response.isError());
    assertEquals("Failed to create room.", response.getError());
  }

  private User createDummyUser() {
    return new User("dummyUid", "dummy@example.com", "Dummy User", "password", LocalDateTime.now());
  }

  private Room createDummyRoom() {
    DisplayUser dummyDisplayUser = new DisplayUser("dummyUid", "Dummy User");
    List<DisplayUser> users = new ArrayList<>();
    users.add(dummyDisplayUser);
    List<Message> messages = new ArrayList<>();
    messages.add(new Message(Instant.now(), "Dummy message", dummyDisplayUser.getUid()));
    return new Room("dummyRoomUid", "Dummy Room", users, messages);
  }
}
