package use_case.settings;

import entities.user_entities.AbstractUser;

public class SettingsInputData {

  AbstractUser user;

  public SettingsInputData(AbstractUser user) {
    this.user = user;
  }

  AbstractUser getUser() {
    return user;
  }
}
