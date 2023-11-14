package use_case.chat;

import interface_adapter.chat.ChatPresenter;

public class ChatInteractor implements ChatInputBoundary {
  final ChatOutputBoundary outputBoundary;
  final ChatMessageDataAccessInterface dataAccessInterface;

  public ChatInteractor(
      ChatOutputBoundary outputBoundary,
      ChatMessageDataAccessInterface dataAccessInterface) {
    this.outputBoundary = outputBoundary;
    this.dataAccessInterface = dataAccessInterface;
  }

  public void loadAllMessages() {
    var messages = dataAccessInterface.getAllMessages();
    outputBoundary.presentMessages(messages);
  }
}
