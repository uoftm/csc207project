package use_case.settings;

import entities.auth.User;

public class SettingsInputData {

  User user;

  String updatedUsername;

  public SettingsInputData(User user) {
    this.user = user;
  }

  public SettingsInputData(String updatedUsername) {
    this.updatedUsername = updatedUsername;
  }

  String getUpdatedUsername(){return updatedUsername;}

  User getUser() {
    return user;
  }
}
