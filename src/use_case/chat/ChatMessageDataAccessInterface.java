package use_case.chat;

import entity.Message;
import java.util.List;

public interface ChatMessageDataAccessInterface {
  void save(Message message);

  List<Message> getAllMessages();
}
