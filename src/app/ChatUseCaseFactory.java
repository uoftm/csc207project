package app;

import interface_adapter.chat.ChatController;
import interface_adapter.chat.ChatPresenter;
import interface_adapter.chat.ChatViewModel;
import use_case.chat.ChatInteractor;
import use_case.rooms.MessageDataAccessInterface;
import use_case.chat.ChatUserDataAccessInterface;
import view.ChatView;

public class ChatUseCaseFactory {
  public static ChatView create(
      MessageDataAccessInterface messageDataAccessObject,
      ChatViewModel chatViewModel,
      ChatUserDataAccessInterface chatUserDataAccessInterface) {
    ChatPresenter chatPresenter = new ChatPresenter(chatViewModel);
    ChatInteractor chatInteractor =
        new ChatInteractor(chatPresenter, messageDataAccessObject, chatUserDataAccessInterface);
    ChatController chatController = new ChatController(chatViewModel, chatInteractor);
    return new ChatView(chatController, chatViewModel);
  }
}
