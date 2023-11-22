package interface_adapter.signup;

public class SignupState {
  private String email = "";
  private String emailError = null;
  private String username = "";
  private String usernameError = null;
  private String password = "";
  private String passwordError = null;
  private String repeatPassword = "";
  private String repeatPasswordError = null;

  public SignupState(SignupState copy) {
    email = copy.email;
    emailError = copy.emailError;
    username = copy.username;
    usernameError = copy.usernameError;
    password = copy.password;
    passwordError = copy.passwordError;
    repeatPassword = copy.repeatPassword;
    repeatPasswordError = copy.repeatPasswordError;
  }

  // Because of the previous copy constructor, the default constructor must be explicit.
  public SignupState() {}

  public String getEmail() {
    return email;
  }
  public String getEmailError() {
    return emailError;
  }
  public String getUsername() {
    return username;
  }

  public String getUsernameError() {
    return usernameError;
  }

  public String getPassword() {
    return password;
  }

  public String getPasswordError() {
    return passwordError;
  }

  public String getRepeatPassword() {
    return repeatPassword;
  }

  public String getRepeatPasswordError() {
    return repeatPasswordError;
  }

  public void setEmail(String email) { this.email = email; }
  public void setEmailError(String emailError) { this.emailError = emailError; }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setUsernameError(String usernameError) {
    this.usernameError = usernameError;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setPasswordError(String passwordError) {
    this.passwordError = passwordError;
  }

  public void setRepeatPassword(String repeatPassword) {
    this.repeatPassword = repeatPassword;
  }

  public void setRepeatPasswordError(String repeatPasswordError) {
    this.repeatPasswordError = repeatPasswordError;
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
