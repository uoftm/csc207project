package use_case.signup;

public class SignupInputData {

  private final String email;
  private final String username;
  private final String password;
  private final String repeatPassword;

  public SignupInputData(String email, String username, String password, String repeatPassword) {
    this.email = email;
    this.username = username;
    this.password = password;
    this.repeatPassword = repeatPassword;
  }

  /**
   * @return the email that the user entered in the signup form
   */
  String getEmail() {
    return email;
  }

  /**
   * @return the username that the user entered in the signup form
   */
  String getUsername() {
    return username;
  }

  /**
   * @return the password that the user entered in the signup form
   */
  String getPassword() {
    return password;
  }

  /**
   * @return the repeated password that the user entered in the signup form
   */
  public String getRepeatPassword() {
    return repeatPassword;
  }
}
