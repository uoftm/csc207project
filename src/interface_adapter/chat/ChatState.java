package interface_adapter.chat;

public class ChatState {
  public String message = "";

  public ChatState(ChatState copy) {
    message = copy.message;
  }

  public ChatState() {}
}
