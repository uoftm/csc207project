package use_case.settings;

public class SettingsOutputData {
  private final String error;

  public SettingsOutputData(String error) {
    this.error = error;
  }

  public boolean getIsError() {
    return error != null;
  }

  public String getError() {
    return error;
  }
}
