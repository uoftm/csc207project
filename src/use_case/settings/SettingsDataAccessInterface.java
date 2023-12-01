package use_case.settings;

import entities.auth.AbstractUser;

public interface SettingsDataAccessInterface {

  String op(AbstractUser user);
}
