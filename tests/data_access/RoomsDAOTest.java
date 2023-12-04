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
    LoginUserDataAccessInterface loginDao = userDao;
    Room testRoom = roomDao.addRoom(testUser, loginDao, "Test Room");

    // Get test room
    Room retrievedRoom = roomDao.getRoomFromId(testUser, loginDao, testRoom.getUid());

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
    roomDao.deleteRoom(testUser, loginDao, testRoom);

    // Confirm room deletion
    Assert.assertThrows(
        RuntimeException.class, () -> roomDao.getRoomFromId(testUser, loginDao, testRoom.getUid()));

    // Delete user
    DeleteUserDataAccessInterface deleteDao = userDao;
    deleteDao.deleteUser(testUser);
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

    dao.addUserToRoom(dummyUser, dummyDisplayUser2, userDao, dummyRoom);
    Room retrievedRoom = dao.getRoomFromId(dummyUser, userDao, dummyRoom.getUid());

    List<DisplayUser> retrievedUsers = retrievedRoom.getUsers();
    Assert.assertEquals(retrievedUsers.size(), 2);
    DisplayUser originalUser, newUser;
    if (retrievedUsers.get(0).getEmail().toLowerCase().equals(dummyUser.getEmail().toLowerCase())) {
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

    User dummyUser2 = addFirebaseDummyUser();
    DisplayUser dummyDisplayUser2 =
        new DisplayUser(dummyUser2.getEmail(), dummyUser2.getPassword());
    dao.addUserToRoom(dummyUser, dummyDisplayUser2, userDao, dummyRoom);

    dao.removeUserFromRoom(dummyUser, dummyDisplayUser2, userDao, dummyRoom);

    Room retrievedRoom = dao.getRoomFromId(dummyUser, userDao, dummyRoom.getUid());
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
    User dummyUser = addFirebaseDummyUser();
    Room room = dao.addRoom(dummyUser, userDao, "New Room");
    assertNotNull(room);

    cleanUpRoom(room, dummyUser);
    cleanUpUser(dummyUser);
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
}
