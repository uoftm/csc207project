package interface_adapter.settings;

import entities.auth.AbstractUser;

public class SettingsState {
  private AbstractUser user;
  private String error = null;

  private Boolean isError;

  private String updatedUsername;

  public SettingsState(SettingsState state) {
    user = state.user;
    error = state.error;
  }

  public SettingsState() {}

  public AbstractUser getUser() {
    return user;
  }

  public void setIsError(Boolean isError){this.isError = isError;}

  public Boolean getIsError(){return isError;}

  public String getUpdatedUsername() {
    return updatedUsername;
  }

  public void setUpdatedUsername(String username) {
    this.updatedUsername = username;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
