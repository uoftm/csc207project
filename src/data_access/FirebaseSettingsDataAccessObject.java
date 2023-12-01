package data_access;

import entities.auth.AbstractUser;
import use_case.settings.SettingsDataAccessInterface;

public class FirebaseSettingsDataAccessObject implements SettingsDataAccessInterface {
  @Override
  public String op(AbstractUser user) {
    return "";
  }
}
