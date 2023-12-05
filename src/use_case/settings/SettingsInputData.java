package use_case.settings;

import entities.auth.User;

public class SettingsInputData {

  User user;

  String updatedUsername;

  public SettingsInputData(String updatedUsername, User user) {
    this.updatedUsername = updatedUsername;
    this.user = user;
  }

  String getUpdatedUsername() {
    return updatedUsername;
  }

  User getUser() {
    return user;
  }
}
