package data_access;

import static org.junit.Assert.assertThrows;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import java.util.List;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.LoggedInDataAccessInterface;
import use_case.rooms.MessageDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;

public class MessagesDAOTest extends DAOTest {
  @Test
  public void testSendAndLoadMessageSuccess() {
    OkHttpClient client = new OkHttpClient();
    MessageDataAccessInterface messageDao = new FirebaseMessageDataAccessObject(client);
    RoomsDataAccessInterface roomDao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    LoggedInDataAccessInterface inMemoryDAO = new InMemoryUserDataAccessObject();
    User dummyUser = addFirebaseDummyUser();
    Room dummyRoom = addFirebaseDummyRoom(dummyUser);
    inMemoryDAO.setUser(dummyUser);
    inMemoryDAO.setIdToken(userDao.getAccessToken(dummyUser.getEmail(), dummyUser.getPassword()));

    Message message = createDummyMessage(dummyUser.toDisplayUser());
    messageDao.sendMessage(dummyRoom, inMemoryDAO, message.getContent());

    String idToken = userDao.getAccessToken(dummyUser.getEmail(), dummyUser.getPassword());
    List<Message> response = roomDao.getRoomFromId(idToken, dummyRoom.getUid()).getMessages();
    Message retrievedMessage = response.get(0);

    Assert.assertTrue(
        message.getTimestamp().toEpochMilli() <= retrievedMessage.getTimestamp().toEpochMilli());
    Assert.assertEquals(message.getContent(), retrievedMessage.getContent());
    Assert.assertEquals(
        message.getDisplayUser().getEmail().toLowerCase(),
        retrievedMessage.getDisplayUser().getEmail().toLowerCase());

    cleanUpRoom(dummyRoom, dummyUser);
    cleanUpUser(dummyUser);
  }

  @Test
  public void testSendMessageFailure() {
    OkHttpClient client = new OkHttpClient();
    MessageDataAccessInterface messageDao = new FirebaseMessageDataAccessObject(client);
    LoggedInDataAccessInterface inMemoryDAO = new InMemoryUserDataAccessObject();

    User invalidUser = createDummyUser();
    Room invalidRoom = createDummyRoom();
    inMemoryDAO.setUser(invalidUser);

    assertThrows(
        RuntimeException.class,
        () -> messageDao.sendMessage(invalidRoom, inMemoryDAO, "Invalid message"));
  }
}
