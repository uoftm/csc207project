package data_access;

import entities.auth.User;
import use_case.rooms.LoggedInDataAccessInterface;

/**
 * This class is an implementation of the LoggedInDataAccessInterface interface. It provides
 * in-memory storage and access to user data and authentication tokens.
 *
 * <p>It is intended to help with passing data round between different views and DAOs
 */
public class InMemoryUserDataAccessObject implements LoggedInDataAccessInterface {

  private User user;
  private String idToken;

  /**
   * Sets the ID token for the logged-in user.
   *
   * @param idToken The ID token to set for the newly created user.
   */
  @Override
  public void setIdToken(String idToken) {
    this.idToken = idToken;
  }

  /**
   * Sets the user for the logged-in session.
   *
   * @param user The User object representing the new logged-in user.
   */
  @Override
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * @return The ID token of the logged-in user.
   * @throws RuntimeException if the authentication failed and the ID token is not set.
   */
  @Override
  public String getIdToken() {
    if (idToken == null) {
      throw new RuntimeException("Authentication failed; please log out and log back in.");
    }
    return idToken;
  }

  /**
   * @throws RuntimeException if the user is not logged in.
   * @return The User object representing the logged-in user.
   */
  @Override
  public User getUser() {
    if (user == null) {
      throw new RuntimeException("User not logged in; please log in again.");
    }
    return user;
  }
}
