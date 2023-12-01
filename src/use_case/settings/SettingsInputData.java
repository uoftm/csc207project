package use_case.settings;

import entities.auth.AbstractUser;

public class SettingsInputData {

  AbstractUser user;

  public SettingsInputData(AbstractUser user) {
    this.user = user;
  }

  AbstractUser getUser() {
    return user;
  }
}
