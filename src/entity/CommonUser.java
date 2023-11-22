package entity;

import java.time.LocalDateTime;

class CommonUser implements User {

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
  CommonUser(String email, String name, String password, LocalDateTime creationTime) {
    this.email = email;
    this.name = name;
    this.password = password;
    this.creationTime = creationTime;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public String getName() {
    return name;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public LocalDateTime getCreationTime() {
    return creationTime;
  }
}
