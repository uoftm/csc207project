package use_case.login;

public class LoginInputData {

  private final String email;
  private final String password;

  public LoginInputData(String email, String password) {
    this.email = email;
    this.password = password;
  }

  String getEmail() {
    return email;
  }

  String getPassword() {
    return password;
  }
}
