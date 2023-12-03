package use_case.rooms;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import java.util.List;

public interface MessageDataAccessInterface {
  void save(Message message);

  List<Message> getAllMessages();

  Response<String> sendMessage(Room dummyRoom, User dummyUser, String message);

  Response<List<Message>> loadMessages(Room dummyRoom, User dummyUser);
}
