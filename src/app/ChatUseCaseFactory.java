package app;

import interface_adapter.chat.ChatController;
import interface_adapter.chat.ChatPresenter;
import interface_adapter.chat.ChatViewModel;
import use_case.chat.ChatInteractor;
import use_case.chat.ChatMessageDataAccessInterface;
import use_case.chat.ChatUserDataAccessInterface;
import view.ChatView;

public class ChatUseCaseFactory {
  /** Prevent instantiation. */
  private ChatUseCaseFactory() {}

  /**
   * Creates a ChatView along with all the clean architecture components required to run it.
   *
   * @param messageDataAccessObject The data access object to load chat messages and send messages.
   * @param chatViewModel
   * @param chatUserDataAccessInterface
   */
  public static ChatView create(
      ChatMessageDataAccessInterface messageDataAccessObject,
      ChatViewModel chatViewModel,
      ChatUserDataAccessInterface chatUserDataAccessInterface) {
    ChatPresenter chatPresenter = new ChatPresenter(chatViewModel);
    ChatInteractor chatInteractor =
        new ChatInteractor(chatPresenter, messageDataAccessObject, chatUserDataAccessInterface);
    ChatController chatController = new ChatController(chatViewModel, chatInteractor);
    return new ChatView(chatController, chatViewModel);
  }
}
