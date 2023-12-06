package data_access;

import entities.auth.User;
import use_case.rooms.LoggedInDataAccessInterface;

public class InMemoryUserDataAccessObject implements LoggedInDataAccessInterface {

  private User user;
  private String idToken;

  @Override
  public void setIdToken(String idToken) {
    this.idToken = idToken;
  }

  @Override
  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String getIdToken() {
    if (idToken == null) {
      throw new RuntimeException("Authentication failed; please log out and log back in.");
    }
    return idToken;
  }

  @Override
  public User getUser() {
    if (user == null) {
      throw new RuntimeException("User not logged in; please log in again.");
    }
    return user;
  }
}
