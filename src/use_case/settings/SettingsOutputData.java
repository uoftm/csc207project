package use_case.settings;

import entities.user_entities.AbstractUser;

public class SettingsOutputData {

  AbstractUser user;
  private final boolean useCaseFailed;

  public SettingsOutputData(AbstractUser user, boolean useCaseFailed) {
    this.user = user;
    this.useCaseFailed = useCaseFailed;
  }

  public AbstractUser getUser() {
    return user;
  }
}
