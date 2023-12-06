package use_case.settings;

import entities.auth.User;

public class SettingsInputData {
  private String newUsername;

  public SettingsInputData(String newUsername) {
    this.newUsername = newUsername;
  }

  String getNewUsername() {
    return newUsername;
  }
}
