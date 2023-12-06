package use_case.settings;

import entities.auth.User;

public interface UserSettingsDataAccessInterface {
  void propogateDisplayNameChange(User user);
}
