package interface_adapter.chat;

import entities.rooms.Message;
import java.util.List;
import use_case.chat.ChatOutputBoundary;

public class ChatPresenter implements ChatOutputBoundary {
  final ChatViewModel viewModel;

  /** Create a ChatPresenter using Clean Architecture. */
  public ChatPresenter(ChatViewModel viewModel) {
    this.viewModel = viewModel;
  }

  /**
   * Adds new messages to the screen.
   *
   * @param messages the list of all messages to display to the screen
   */
  public void presentMessages(List<Message> messages) {
    viewModel.messages = messages;
    viewModel.firePropertyChanged();
  }
}
