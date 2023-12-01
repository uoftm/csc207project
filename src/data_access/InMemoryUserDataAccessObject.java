package data_access;

import entities.user_entities.User;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import use_case.signup.SignupUserDataAccessInterface;

public class InMemoryUserDataAccessObject implements SignupUserDataAccessInterface {

  private final Map<String, User> users = new HashMap<>();

  /**
   * @param identifier the user's username
   * @return whether the user exists
   */
  @Override
  public boolean existsByName(String identifier) {
    return users.containsKey(identifier);
  }

  /**
   * @param user the data to save
   */
  @Override
  public Optional<String> save(User user) {
    users.put(user.getName(), user);
    return Optional.empty();
  }
}
