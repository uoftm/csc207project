package interface_adapter.signup;

public class SignupState {
  private String email = "";
  private String username = "";
  private String error = null;
  private String password = "";
  private String repeatPassword = "";

  public SignupState(SignupState copy) {
    email = copy.email;
    username = copy.username;
    error = copy.error;
    password = copy.password;
    repeatPassword = copy.repeatPassword;
  }

  // Because of the previous copy constructor, the default constructor must be explicit.
  public SignupState() {}

  public String getEmail() {
    return email;
  }
  public String getUsername() {
    return username;
  }

  public String getError() {
    return error;
  }

  public String getPassword() {
    return password;
  }

  public String getRepeatPassword() {
    return repeatPassword;
  }

  public void setEmail(String email) { this.email = email; }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setError(String error) {
    this.error = error;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setRepeatPassword(String repeatPassword) {
    this.repeatPassword = repeatPassword;
  }

  @Override
  public String toString() {
    return "SignupState{"
        + "email='"
        + email
        + '\''
        + "username='"
        + username
        + '\''
        + ", password='"
        + password
        + '\''
        + ", repeatPassword='"
        + repeatPassword
        + '\''
        + '}';
  }
}
