package use_case.settings;

import entity.User;

public class SettingsOutputData {

  User user;
  private final boolean useCaseFailed;

  public SettingsOutputData(User user, boolean useCaseFailed) {
    this.user = user;
    this.useCaseFailed = useCaseFailed;
  }

  public User getUser() {
    return user;
  }
}
