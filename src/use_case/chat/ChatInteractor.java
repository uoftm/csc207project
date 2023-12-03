package use_case.chat;

import entities.rooms.Message;
import use_case.rooms.MessageDataAccessInterface;

import java.time.Instant;

public class ChatInteractor implements ChatInputBoundary {
  final ChatOutputBoundary outputBoundary;
  final MessageDataAccessInterface dataAccessInterface;
  final ChatUserDataAccessInterface chatUserDataAccessInterface;

  public ChatInteractor(
      ChatOutputBoundary outputBoundary,
      MessageDataAccessInterface dataAccessInterface,
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
        new Message(Instant.now(), messageText, chatUserDataAccessInterface.get().getEmail());
    dataAccessInterface.save(message);
  }
}
