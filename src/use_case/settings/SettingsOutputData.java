package use_case.settings;

public class SettingsOutputData {
  private String error;

  public SettingsOutputData(String error) {
    this.error = error;
  }

  public String getError() {
    return error;
  }
}
