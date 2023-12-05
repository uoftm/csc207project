package use_case.settings;

import entities.auth.User;
import entities.settings.SettingsChangeResponse;

public interface UserSettingsDataAccessInterface {
  void changeDisplayName(User user, String newDisplayName);
}
