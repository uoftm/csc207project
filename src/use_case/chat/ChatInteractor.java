package use_case.chat;

import entities.Message;
import java.time.Instant;

public class ChatInteractor implements ChatInputBoundary {
  final ChatOutputBoundary outputBoundary;
  final ChatMessageDataAccessInterface dataAccessInterface;
  final ChatUserDataAccessInterface chatUserDataAccessInterface;

  public ChatInteractor(
      ChatOutputBoundary outputBoundary,
      ChatMessageDataAccessInterface dataAccessInterface,
      ChatUserDataAccessInterface chatUserDataAccessInterface) {
    this.outputBoundary = outputBoundary;
    this.dataAccessInterface = dataAccessInterface;
    this.chatUserDataAccessInterface = chatUserDataAccessInterface;
  }

  public void loadAllMessages() {
    var messages = dataAccessInterface.getAllMessages();
    outputBoundary.presentMessages(messages);
  }

  public void sendMessage(String messageText) {
    Message message =
        new Message(Instant.now(), messageText, chatUserDataAccessInterface.get().getUid());
    dataAccessInterface.save(message);
  }
}
