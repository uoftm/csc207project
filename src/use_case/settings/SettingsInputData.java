package use_case.settings;

import entities.auth.User;

public class SettingsInputData {

  private User user;

  private String newUsername;

  public SettingsInputData(String newUsername, User user) {
    this.newUsername = newUsername;
    this.user = user;
  }

  String getNewUsername() {
    return newUsername;
  }

  User getUser() {
    return user;
  }
}
