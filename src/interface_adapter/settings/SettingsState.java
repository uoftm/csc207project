package interface_adapter.settings;

import entities.user_entities.AbstractUser;

public class SettingsState {
  private AbstractUser user;
  private String error = null;

  public SettingsState(SettingsState state) {
    user = state.user;
    error = state.error;
  }

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
