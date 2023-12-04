package use_case.settings;

import entities.auth.AbstractUser;

public class SettingsInputData {

  AbstractUser user;

  String updatedUsername;

  public SettingsInputData(AbstractUser user) {
    this.user = user;
  }

  public SettingsInputData(String updatedUsername) {
    this.updatedUsername = updatedUsername;
  }

  AbstractUser getUser() {
    return user;
  }
}
