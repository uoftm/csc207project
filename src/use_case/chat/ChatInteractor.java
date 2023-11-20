package use_case.chat;

import entity.Message;
import java.time.Instant;

public class ChatInteractor implements ChatInputBoundary {
  final ChatOutputBoundary outputBoundary;
  final ChatMessageDataAccessInterface dataAccessInterface;

  public ChatInteractor(
      ChatOutputBoundary outputBoundary, ChatMessageDataAccessInterface dataAccessInterface) {
    this.outputBoundary = outputBoundary;
    this.dataAccessInterface = dataAccessInterface;
  }

  public void loadAllMessages() {
    var messages = dataAccessInterface.getAllMessages();
    outputBoundary.presentMessages(messages);
  }

  public void sendMessage(String messageText) {
    Message message = new Message(Instant.now(), messageText);
    dataAccessInterface.save(message);
  }
}
