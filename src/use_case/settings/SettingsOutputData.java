package use_case.settings;

public class SettingsOutputData {
  private String error;
  private boolean isError;

  public SettingsOutputData(String error, boolean isError) {
    this.error = error;
    this.isError = isError;
  }

  public boolean getIsError() {
    return isError;
  }

  public String getError() {
    return error;
  }
}
