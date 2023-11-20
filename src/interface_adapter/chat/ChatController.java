package interface_adapter.chat;

import entity.Message;
import use_case.chat.ChatInputBoundary;

public class ChatController {
  final ChatViewModel viewModel;

  final ChatInputBoundary chatInputBoundary;

  public ChatController(ChatViewModel viewModel, ChatInputBoundary chatInputBoundary) {
    this.viewModel = viewModel;
    this.chatInputBoundary = chatInputBoundary;
  }

  public void loadAllMessages() {
    chatInputBoundary.loadAllMessages();
  }

  public void sendMessage(Message message) {
    chatInputBoundary.sendMessage(message);
  }
}
