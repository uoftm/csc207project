package use_case.chat;

import entities.Message;
import java.util.List;

public interface ChatMessageDataAccessInterface {
  void save(Message message);

  List<Message> getAllMessages();
}
