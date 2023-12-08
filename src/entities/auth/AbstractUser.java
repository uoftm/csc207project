package entities.auth;

/**
 * The AbstractUser interface represents a generic user in the system. It provides methods to
 * retrieve the user's email and name.
 */
public interface AbstractUser {
  /**
   * @return The email address of the user as a string.
   */
  String getEmail();

  /**
   * @return The name/username of the user
   */
  String getName();
}
