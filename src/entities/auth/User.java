package entities.auth;

import java.time.LocalDateTime;

public record User(
    String uid, String email, String name, String password, LocalDateTime creationTime)
    implements AbstractUser {
  /**
   * Requires: password is valid.
   *
   * @param name
   * @param password
   */
  public User {}

  public DisplayUser toDisplayUser() {
    return new DisplayUser(this.email(), this.name());
  }
}
