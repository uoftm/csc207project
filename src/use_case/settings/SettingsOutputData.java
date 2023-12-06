package use_case.settings;

public class SettingsOutputData {
  private String error;

  private String username;

  public SettingsOutputData(String error, String username) {
    this.error = error;
    this.username = username;
  }

  public String getError() {
    return error;
  }

  public String getUsername() {
    return username;
  }
}
