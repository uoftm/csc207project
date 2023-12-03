package data_access;

import entities.auth.User;
import java.util.UUID;

import entities.rooms.Room;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;
import use_case.settings.DeleteUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class RoomDAOTest {
  // Currently, the tests in this class cover the signup portion of the Firebase User DAO code
  // However, they don't clean up after themselves.
  // TODO: Write a delete user function that scrubs all user data
  //  (e.g. display name, membership list of room, deletes account)
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
    Assert.assertEquals(testRoom.getMessages(), retrievedRoom.getMessages());
    Assert.assertEquals(testRoom.getUsers(), retrievedRoom.getUsers());

    // Delete test room
    roomDao.deleteRoom(testUser, loginDao, testRoom);

    // Confirm room deletion
    Assert.assertThrows(
            RuntimeException.class, () -> roomDao.getRoomFromId(testUser, loginDao, testRoom.getUid()));

    // Delete user
    DeleteUserDataAccessInterface deleteDao = userDao;
    deleteDao.deleteUser(testUser);
  }
}
