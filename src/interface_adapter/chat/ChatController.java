package interface_adapter.chat;

import use_case.chat.ChatInputBoundary;

public class ChatController {
  final ChatViewModel viewModel;

  final ChatInputBoundary chatInputBoundary;

  /** Create a ChatController using Clean Architecture. */
  public ChatController(ChatViewModel viewModel, ChatInputBoundary chatInputBoundary) {
    this.viewModel = viewModel;
    this.chatInputBoundary = chatInputBoundary;
  }

  /** Loads all messages through the chatInputBoundary. */
  public void loadAllMessages() {
    chatInputBoundary.loadAllMessages();
  }

  /**
   * @param messageText the message to send
   */
  public void sendMessage(String messageText) {
    chatInputBoundary.sendMessage(messageText);
  }
}
