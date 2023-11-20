package app;

import interface_adapter.chat.ChatController;
import interface_adapter.chat.ChatPresenter;
import interface_adapter.chat.ChatViewModel;
import java.util.ArrayList;
import use_case.chat.ChatInteractor;
import use_case.chat.ChatMessageDataAccessInterface;
import view.ChatView;

public class ChatUseCaseFactory {
  public static ChatView create(ChatMessageDataAccessInterface messageDataAccessObject) {
    var chatViewModel = new ChatViewModel(new ArrayList<>());
    ChatPresenter chatPresenter = new ChatPresenter(chatViewModel);
    ChatInteractor chatInteractor = new ChatInteractor(chatPresenter, messageDataAccessObject);
    ChatController chatController = new ChatController(chatViewModel, chatInteractor);
    return new ChatView(chatController, chatViewModel);
  }
}
