package use_case.chat;

public interface ChatInputBoundary {
  void loadAllMessages();

  void sendMessage(String messageText);
}
