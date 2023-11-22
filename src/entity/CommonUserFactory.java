package entity;

import java.time.LocalDateTime;

public class CommonUserFactory implements UserFactory {
  /**
   * Requires: password is valid.
   *
   * @param name
   * @param password
   * @return
   */
  @Override
  public User create(String email, String name, String password, LocalDateTime ltd) {
    return new CommonUser(email, name, password, ltd);
  }
}
