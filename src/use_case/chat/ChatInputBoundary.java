package use_case.chat;

import entity.Message;

public interface ChatInputBoundary {
  void loadAllMessages();
  void sendMessage(Message message);
}
