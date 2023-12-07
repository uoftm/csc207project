package use_case.settings;

public class SettingsOutputData {
  private final String error;

  private final String username;

  public SettingsOutputData(String error, String username) {
    this.error = error;
    this.username = username;
  }

  public boolean getIsError() {
    return error != null;
  }

  public String getError() {
    return error;
  }

  public String getUsername() {
    return username;
  }
}
