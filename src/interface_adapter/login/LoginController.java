package interface_adapter.login;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;

public class LoginController {

  final LoginInputBoundary loginUseCaseInteractor;

  public LoginController(LoginInputBoundary loginUseCaseInteractor) {
    this.loginUseCaseInteractor = loginUseCaseInteractor;
  }

  /**
   * Login with the given email and password.
   *
   * @param email The email of the user.
   * @param password The password of the user.
   */
  public void execute(String email, String password) {
    LoginInputData loginInputData = new LoginInputData(email, password);
    loginUseCaseInteractor.execute(loginInputData);
  }
}
