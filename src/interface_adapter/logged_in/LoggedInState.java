package interface_adapter.logged_in;

import entities.auth.User;

public class LoggedInState {
  User user;

  public LoggedInState(LoggedInState copy) {
    user = copy.user;
  }

  // Because of the previous copy constructor, the default constructor must be explicit.
  public LoggedInState() {}

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
