package use_case.settings;

import entities.user_entities.AbstractUser;

public interface SettingsDataAccessInterface {

  String op(AbstractUser user);
}
