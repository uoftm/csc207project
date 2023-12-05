package interface_adapter.logged_in;

import entities.auth.User;

public class LoggedInState {
  private String username = "";
  User user;

  public LoggedInState(LoggedInState copy) {
    username = copy.username;
    user = copy.user;
  }

  // Because of the previous copy constructor, the default constructor must be explicit.
  public LoggedInState() {}

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
