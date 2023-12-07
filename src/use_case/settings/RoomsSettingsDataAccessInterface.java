package use_case.settings;

import entities.auth.User;

public interface RoomsSettingsDataAccessInterface {
  void propogateDisplayNameChange(String idToken, User user);
}
