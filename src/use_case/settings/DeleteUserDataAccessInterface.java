package use_case.settings;

import entities.auth.User;

/**
 * The DeleteUserDataAccessInterface is an interface that defines the SOLID interface for deleting
 * user data.
 */
public interface DeleteUserDataAccessInterface {
  /**
   * Deletes a user by their ID token and user object.
   *
   * @param idToken - the ID token of the user.
   * @param user - the user object to be deleted.
   */
  void deleteUser(String idToken, User user);
}
