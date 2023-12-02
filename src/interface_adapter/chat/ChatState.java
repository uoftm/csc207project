package interface_adapter.chat;

public class ChatState {
  private String message = "";

  public ChatState() {}

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
