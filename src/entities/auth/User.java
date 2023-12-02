package entities.auth;

import java.time.LocalDateTime;

public class User extends DisplayUser implements AbstractUser {
  private final String email;
  private final String password;
  private final LocalDateTime creationTime;

  /**
   * Requires: password is valid.
   *
   * @param name
   * @param password
   */
  public User(String uid, String email, String name, String password, LocalDateTime creationTime) {
    super(uid, name);
    this.email = email;
    this.password = password;
    this.creationTime = creationTime;
  }

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public LocalDateTime getCreationTime() {
    return creationTime;
  }
}
