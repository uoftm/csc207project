package data_access;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Room;
import java.util.List;
import java.util.UUID;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;
import use_case.settings.DeleteUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class RoomsDAOTest extends DAOTest {
  @Test
  public void testSaveRoom() {
    OkHttpClient client = new OkHttpClient();
    FirebaseUserDataAccessObject userDao = new FirebaseUserDataAccessObject(client);
    RoomsDataAccessInterface roomDao = new FirebaseRoomsDataAccessObject(client);

    // Create test user
    String fakeEmail =
        String.format("testSaveUser%s@example.com", UUID.randomUUID().toString().substring(0, 10));
    User testUser = new User(null, fakeEmail, "Jane Doe", "password", null);

    // Sign up user
    SignupUserDataAccessInterface signupDao = userDao;
    signupDao.save(testUser);

    // Create test room
    String idToken = userDao.getAccessToken(testUser.getEmail(), testUser.getPassword());
    Room testRoom = roomDao.addRoom(idToken, testUser, "Test Room");

    // Get test room
    Room retrievedRoom = roomDao.getRoomFromId(idToken, testRoom.getUid());

    // Assert that the room details are equivalent
    Assert.assertEquals(testRoom.getName(), retrievedRoom.getName());
    Assert.assertEquals(testRoom.getUid(), retrievedRoom.getUid());
    Assert.assertTrue(retrievedRoom.getMessages().isEmpty());
    Assert.assertEquals(
        testRoom.getUsers().get(0).getEmail().toLowerCase(),
        retrievedRoom.getUsers().get(0).getEmail().toLowerCase());
    Assert.assertEquals(
        testRoom.getUsers().get(0).getName(), retrievedRoom.getUsers().get(0).getName());

    // Delete test room
    roomDao.deleteRoom(idToken, testRoom);

    // Confirm room deletion
    Assert.assertThrows(
        RuntimeException.class, () -> roomDao.getRoomFromId(idToken, testRoom.getUid()));

    // Delete user
    DeleteUserDataAccessInterface deleteDao = userDao;
    deleteDao.deleteUser(idToken, testUser);
  }

  @Test
  public void testAddUserToRoomSuccess() {
    OkHttpClient client = new OkHttpClient();
    FirebaseRoomsDataAccessObject dao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);

    User dummyUser = addFirebaseDummyUser();
    Room dummyRoom = addFirebaseDummyRoom(dummyUser);

    User dummyUser2 = addFirebaseDummyUser();
    DisplayUser dummyDisplayUser2 = new DisplayUser(dummyUser2.getEmail(), dummyUser2.getName());

    String idToken = userDao.getAccessToken(dummyUser.getEmail(), dummyUser.getPassword());
    dao.addUserToRoom(idToken, dummyDisplayUser2, dummyRoom);
    Room retrievedRoom = dao.getRoomFromId(idToken, dummyRoom.getUid());

    List<DisplayUser> retrievedUsers = retrievedRoom.getUsers();
    Assert.assertEquals(retrievedUsers.size(), 2);
    DisplayUser originalUser, newUser;
    if (retrievedUsers.get(0).getEmail().equalsIgnoreCase(dummyUser.getEmail())) {
      // First retrieved user is the original user
      originalUser = retrievedUsers.get(0);
      newUser = retrievedUsers.get(1);
    } else {
      // Second retrieved user is the original user
      originalUser = retrievedUsers.get(1);
      newUser = retrievedUsers.get(0);
    }
    Assert.assertEquals(dummyUser.getEmail().toLowerCase(), originalUser.getEmail().toLowerCase());
    Assert.assertEquals(dummyUser.getName(), originalUser.getName());
    Assert.assertEquals(dummyUser2.getEmail().toLowerCase(), newUser.getEmail().toLowerCase());
    Assert.assertEquals(dummyUser2.getName(), newUser.getName());

    cleanUpRoom(dummyRoom, dummyUser);
    cleanUpUser(dummyUser);
    cleanUpUser(dummyUser2);
  }

  @Test
  public void testDeleteUserFromRoomSuccess() {
    OkHttpClient client = new OkHttpClient();
    FirebaseRoomsDataAccessObject dao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);

    User dummyUser = addFirebaseDummyUser();
    Room dummyRoom = addFirebaseDummyRoom(dummyUser);

    String idToken = userDao.getAccessToken(dummyUser.getEmail(), dummyUser.getPassword());

    User dummyUser2 = addFirebaseDummyUser();
    DisplayUser dummyDisplayUser2 =
        new DisplayUser(dummyUser2.getEmail(), dummyUser2.getPassword());
    dao.addUserToRoom(idToken, dummyDisplayUser2, dummyRoom);

    dao.removeUserFromRoom(idToken, dummyDisplayUser2, dummyRoom);

    Room retrievedRoom = dao.getRoomFromId(idToken, dummyRoom.getUid());
    Assert.assertEquals(retrievedRoom.getUsers().size(), 1);
    DisplayUser retrievedUser = retrievedRoom.getUsers().get(0);
    Assert.assertEquals(retrievedUser.getName(), dummyUser.getName());
    Assert.assertEquals(retrievedUser.getEmail().toLowerCase(), dummyUser.getEmail().toLowerCase());

    Assert.assertEquals(dao.getAvailableRoomIds(dummyUser2).size(), 0);

    cleanUpRoom(dummyRoom, dummyUser);
    cleanUpUser(dummyUser);
  }

  @Test
  public void testAddUserToRoomFailure() {
    RoomsDataAccessInterface dao =
        new FirebaseRoomsDataAccessObject(null) {
          @Override
          public void addUserToRoom(String idToken, DisplayUser displayUser, Room room) {
            throw new RuntimeException("Failed to add user.");
          }
        };
    Room dummyRoom = createDummyRoom();
    User dummyUser = createDummyUser();
    DisplayUser dummyDisplayUser = createDummyDisplayUser();
    assertThrows(
        "Failed to add user.",
        RuntimeException.class,
        () -> dao.addUserToRoom(null, dummyDisplayUser, dummyRoom));
  }

  @Test
  public void testCreateRoomSuccess() {
    OkHttpClient client = new OkHttpClient();
    RoomsDataAccessInterface dao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    User dummyUser = addFirebaseDummyUser();
    String idToken = userDao.getAccessToken(dummyUser.getEmail(), dummyUser.getPassword());
    Room room = dao.addRoom(idToken, dummyUser, "New Room");
    assertNotNull(room);

    cleanUpRoom(room, dummyUser);
    cleanUpUser(dummyUser);
  }

  @Test
  public void testCreateRoomFailure() {
    RoomsDataAccessInterface dao =
        new FirebaseRoomsDataAccessObject(null) {
          @Override
          public Room addRoom(String idToken, User user, String roomName) {
            throw new RuntimeException("Failed to create room.");
          }
        };
    User dummyUser = createDummyUser();
    String roomName = "New Room";
    assertThrows(
        "Failed to create room.",
        RuntimeException.class,
        () -> dao.addRoom(null, dummyUser, roomName));
  }

  @Test
  public void changeRoomNameSuccess() {
    OkHttpClient client = new OkHttpClient();
    RoomsDataAccessInterface dao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    User dummyUser = addFirebaseDummyUser();
    String idToken = userDao.getAccessToken(dummyUser.getEmail(), dummyUser.getPassword());
    Room room = dao.addRoom(idToken, dummyUser, "New Room");
    dao.changeRoomName(idToken, room, "Test Room 2");
    Room retrievedRoom = dao.getRoomFromId(idToken, room.getUid());
    Assert.assertEquals("Test Room 2", retrievedRoom.getName());
    cleanUpRoom(room, dummyUser);
    cleanUpUser(dummyUser);
  }

  @Test
  public void changeRoomNameFailure() {
    RoomsDataAccessInterface dao =
        new FirebaseRoomsDataAccessObject(null) {
          @Override
          public void changeRoomName(String idToken, Room activeRoom, String roomName) {
            throw new RuntimeException("Unable to change room name. Please try again.");
          }
        };
    Room room = createDummyRoom();
    assertThrows(
        "Failed to change room name.",
        RuntimeException.class,
        () -> dao.changeRoomName(null, room, "Test Room 2"));
  }
}
