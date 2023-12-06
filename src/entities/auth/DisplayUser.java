package entities.auth;

public final class DisplayUser implements AbstractUser {
  private final String email;
  private final String name;

  public DisplayUser(String email, String name) {
    this.email = email;
    this.name = name;
  }

  @Override
  public String getEmail() {
    return email;
  }

  @Override
  public String getName() {
    return name;
  }
}
