package entities.auth;

/**
 * The DisplayUser class represents a user in the system with their email and name. It implements
 * the AbstractUser interface and provides methods to retrieve the user's email and name.
 */
public class DisplayUser implements AbstractUser {
  private final String email;
  private final String name;

  /**
   * Constructs a new DisplayUser object with the given email and name.
   *
   * @param email The email of the user.
   * @param name The name of the user.
   */
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
