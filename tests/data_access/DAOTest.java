package data_access;

import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import okhttp3.OkHttpClient;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public abstract class DAOTest {
  Message createDummyMessage(DisplayUser dummyDisplayUser) {
    return new Message(Instant.now(), "Dummy message", dummyDisplayUser);
  }

  public static User createDummyUser() {
    String fakeEmail =
        String.format("testSaveUser%s@example.com", UUID.randomUUID().toString().substring(0, 10));
    return new User(null, fakeEmail, "Dummy User", "password", LocalDateTime.now());
  }

  DisplayUser createDummyDisplayUser() {
    return new DisplayUser("dummy@example.com", "dummyUid");
  }

  public static Room createDummyRoom() {
    DisplayUser dummyDisplayUser = new DisplayUser("dummyUid", "Dummy User");
    List<DisplayUser> users = new ArrayList<>();
    users.add(dummyDisplayUser);
    List<Message> messages = new ArrayList<>();
    messages.add(new Message(Instant.now(), "Dummy message", dummyDisplayUser));
    return new Room("dummyRoomUid", "Dummy Room", users, messages);
  }

  /**
   * This adds a dummy user to Firebase and returns that user The caller *must* clean up the user
   * after use
   */
  public static User addFirebaseDummyUser() {
    User dummyUser = createDummyUser();
    OkHttpClient client = new OkHttpClient();
    SignupUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    userDao.save(dummyUser);
    return dummyUser;
  }

  void cleanUpUser(User user) {
    OkHttpClient client = new OkHttpClient();
    FirebaseUserDataAccessObject userDao = new FirebaseUserDataAccessObject(client);
    String idToken = userDao.getAccessToken(user.getEmail(), user.getPassword());
    userDao.deleteUser(idToken, user);
  }

  void cleanUpRoom(Room room, User user) {
    OkHttpClient client = new OkHttpClient();
    RoomsDataAccessInterface roomsDao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface loginDao = new FirebaseUserDataAccessObject(client);
    String idToken = loginDao.getAccessToken(user.getEmail(), user.getPassword());
    roomsDao.deleteRoom(idToken, user, room);
  }

  /**
   * This adds a dummy room to Firebase and returns that room The caller *must* clean up the room
   * after use
   */
  public static Room addFirebaseDummyRoom(User user) {
    OkHttpClient client = new OkHttpClient();
    RoomsDataAccessInterface roomsDao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    String idToken = userDao.getAccessToken(user.getEmail(), user.getPassword());
    return roomsDao.addRoom(idToken, user, "Dummy Room");
  }
}
