package interface_adapter.settings;

import entities.auth.AbstractUser;

public class SettingsState {
  private AbstractUser user;
  private String error = null;

  public SettingsState() {}

  public AbstractUser getUser() {
    return user;
  }

  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }
}
