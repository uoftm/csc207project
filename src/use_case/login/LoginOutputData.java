package use_case.login;

import entities.auth.User;

public class LoginOutputData {
  private final User user;
  private final boolean useCaseFailed;

  public LoginOutputData(User user, boolean useCaseFailed) {
    this.user = user;
    this.useCaseFailed = useCaseFailed;
  }

  public String getUsername() {
    return user.getName();
  }

  public String getUid() {
    return user.getUid();
  }

  public User getUser() {
    return user;
  }
}
