package data_access;

import entities.auth.User;
import java.util.HashMap;
import java.util.Map;
import use_case.signup.SignupUserDataAccessInterface;

public class InMemoryUserDataAccessObject implements SignupUserDataAccessInterface {

  private final Map<String, User> users = new HashMap<>();

  /**
   * @param identifier the user's username
   * @return whether the user exists
   */

  /**
   * @param user the data to save
   */
  @Override
  public void save(User user) {
    users.put(user.name(), user);
  }
}
