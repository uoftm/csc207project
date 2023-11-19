package interface_adapter.logged_in;

import interface_adapter.chat.ChatState;

public class LoggedInState {
  private String username = "";

  private ChatState state = new ChatState();

  public LoggedInState(LoggedInState copy) {
    username = copy.username;
  }

  // Because of the previous copy constructor, the default constructor must be explicit.
  public LoggedInState() {}

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
}
