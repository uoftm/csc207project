package interface_adapter.chat;

import entities.Message;
import java.util.List;
import use_case.chat.ChatOutputBoundary;

public class ChatPresenter implements ChatOutputBoundary {
  final ChatViewModel viewModel;

  public ChatPresenter(ChatViewModel viewModel) {
    this.viewModel = viewModel;
  }

  public void presentMessages(List<Message> messages) {
    viewModel.messages = messages;
    viewModel.firePropertyChanged();
  }
}
