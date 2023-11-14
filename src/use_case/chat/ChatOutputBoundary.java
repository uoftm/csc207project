package use_case.chat;

import entity.Message;
import java.util.List;

public interface ChatOutputBoundary {
  public void presentMessages(List<Message> messages);
}
