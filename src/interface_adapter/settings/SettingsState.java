package interface_adapter.settings;

public class SettingsState {
  private String message = null;

  private Boolean isError = false;
  private Boolean isSuccess = false;

  private String updatedUsername;

  public SettingsState(SettingsState state) {
    message = state.message;
    isError = state.isError;
    isSuccess = state.isSuccess;
    updatedUsername = state.updatedUsername;
  }

  public SettingsState() {}

  public void setIsError(Boolean isError) {
    this.isError = isError;
  }

  public Boolean getIsError() {
    return isError;
  }

  public void setIsSuccess(Boolean isSuccess) {
    this.isSuccess = isSuccess;
  }

  public Boolean getIsSuccess() {
    return isSuccess;
  }

  public String getUpdatedUsername() {
    return updatedUsername;
  }

  public void setUpdatedUsername(String username) {
    this.updatedUsername = username;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
