package use_case.chat;

import entities.rooms.Message;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import use_case.rooms.MessageDataAccessInterface;

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
    // TODO: Delete this function
    List<Message> messages = new ArrayList<>();
    outputBoundary.presentMessages(messages);
  }

  public void sendMessage(String messageText) {
    // TODO: Delete this function
    Message message =
        new Message(Instant.now(), messageText, chatUserDataAccessInterface.get().getEmail());
    // dataAccessInterface.save(message);
  }
}
