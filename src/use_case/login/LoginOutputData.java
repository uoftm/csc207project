package use_case.login;

public class LoginOutputData {
  private final String username;
  private final String userUid;

  private final boolean useCaseFailed;

  public LoginOutputData(String username, String userUid, boolean useCaseFailed) {
    this.username = username;
    this.userUid = userUid;
    this.useCaseFailed = useCaseFailed;
  }

  public String getUsername() {
    return username;
  }
  public String getUid() {
    return userUid;
  }
}
