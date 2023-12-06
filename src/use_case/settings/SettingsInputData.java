package use_case.settings;

public class SettingsInputData {
  private String newUsername;

  public SettingsInputData(String newUsername) {
    this.newUsername = newUsername;
  }

  String getNewUsername() {
    return newUsername;
  }
}
