package use_case.settings;

import entities.auth.User;
import use_case.login.LoginUserDataAccessInterface;

public interface RoomsSettingsDataAccessInterface {
  void propogateDisplayNameChange(User user, LoginUserDataAccessInterface userDao);
}
