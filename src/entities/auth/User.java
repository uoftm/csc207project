package entities.auth;

import java.time.LocalDateTime;

public class User implements AbstractUser {
  private final String uid;
  private final String email;
  private final String name;
  private final String password;
  private final LocalDateTime creationTime;

  /**
   * Requires: password is valid.
   *
   * @param name
   * @param password
   */
  public User(String uid, String email, String name, String password, LocalDateTime creationTime) {
    this.uid = uid;
    this.email = email;
    this.name = name;
    this.password = password;
    this.creationTime = creationTime;
  }

  public String getUid() {
    return uid;
  }

  @Override
  public String getName() {
    return name;
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

  public DisplayUser toDisplayUser() {
    return new DisplayUser(this.getEmail(), this.getName());
  }
}
