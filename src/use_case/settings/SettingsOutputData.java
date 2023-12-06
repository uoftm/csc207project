package use_case.settings;

import entities.auth.AbstractUser;

public class SettingsOutputData {

  final AbstractUser user;
  private final boolean useCaseFailed;

  public SettingsOutputData(AbstractUser user, boolean useCaseFailed) {
    this.user = user;
    this.useCaseFailed = useCaseFailed;
  }

  public AbstractUser getUser() {
    return user;
  }
}
