package entities.auth;

public class DisplayUser implements AbstractUser {
  private final String email;
  private final String name;

  public DisplayUser(String email, String name) {
    this.email = email;
    this.name = name;
  }

  public String getEmail() {
    return email;
  }
  public String getName() {
    return name;
  }
}
