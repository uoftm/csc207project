package entities.auth;

import java.time.Instant;

public class User extends DisplayUser implements AbstractUser {
  private final String email;
  private final String password;
  private final Instant creationTime;

  /**
   * Requires: password is valid.
   *
   * @param name
   * @param password
   */
  public User(String uid, String email, String name, String password, Instant creationTime) {
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

  public Instant getCreationTime() {
    return creationTime;
  }
}
