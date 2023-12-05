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
import use_case.rooms.MessageDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;

public class MessagesDAOTest extends DAOTest {
  @Test
  public void testSendAndLoadMessageSuccess() {
    OkHttpClient client = new OkHttpClient();
    MessageDataAccessInterface messageDao = new FirebaseMessageDataAccessObject(client);
    RoomsDataAccessInterface roomDao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    User dummyUser = addFirebaseDummyUser();
    Room dummyRoom = addFirebaseDummyRoom(dummyUser);

    Message message = createDummyMessage(dummyUser.toDisplayUser());
    messageDao.sendMessage(dummyRoom, userDao, dummyUser, message.content);

    List<Message> response =
        roomDao.getRoomFromId(dummyUser, userDao, dummyRoom.getUid()).getMessages();
    Message retrievedMessage = response.get(0);

    Assert.assertTrue(
        message.timestamp.toEpochMilli() <= retrievedMessage.timestamp.toEpochMilli());
    Assert.assertEquals(message.content, retrievedMessage.content);
    Assert.assertEquals(
        message.displayUser.getEmail().toLowerCase(),
        retrievedMessage.displayUser.getEmail().toLowerCase());

    cleanUpRoom(dummyRoom, dummyUser);
    cleanUpUser(dummyUser);
  }

  @Test
  public void testSendMessageFailure() {
    OkHttpClient client = new OkHttpClient();
    MessageDataAccessInterface messageDao = new FirebaseMessageDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);

    User invalidUser = createDummyUser();
    Room invalidRoom = createDummyRoom();

    assertThrows(
        RuntimeException.class,
        () -> messageDao.sendMessage(invalidRoom, userDao, invalidUser, "Invalid message"));
  }
}
