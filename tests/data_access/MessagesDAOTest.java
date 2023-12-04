package data_access;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.MessageDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;

import java.util.List;

import static org.junit.Assert.assertThrows;

public class MessagesDAOTest extends DAOTest {
    @Test
    public void testSendAndLoadMessageSuccess() {
        OkHttpClient client = new OkHttpClient();
        MessageDataAccessInterface messageDao = new FirebaseMessageDataAccessObject(client);
        RoomsDataAccessInterface roomDao = new FirebaseRoomsDataAccessObject(client);
        LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
        User dummyUser = addFirebaseDummyUser();
        Room dummyRoom = addFirebaseDummyRoom(dummyUser);

        Message message = createDummyMessage();
        messageDao.sendMessage(dummyRoom, userDao, dummyUser, message);

        List<Message> response =
                roomDao.getRoomFromId(dummyUser, userDao, dummyRoom.getUid()).getMessages();
        Message retrievedMessage = response.get(0);

        Assert.assertTrue(message.timestamp.toEpochMilli() <= retrievedMessage.timestamp.toEpochMilli());
        Assert.assertEquals(message.content, retrievedMessage.content);
        Assert.assertEquals(message.authorEmail.toLowerCase(), retrievedMessage.authorEmail.toLowerCase());

        cleanUpRoom(dummyRoom, dummyUser);
        cleanUpUser(dummyUser);
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
}
