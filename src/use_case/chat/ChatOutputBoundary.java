package use_case.chat;

import entities.Message;
import java.util.List;

public interface ChatOutputBoundary {
  public void presentMessages(List<Message> messages);
}
