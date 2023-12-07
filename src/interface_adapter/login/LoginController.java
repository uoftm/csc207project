package interface_adapter.login;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;

public class LoginController {
  private final LoginInputBoundary loginUseCaseInteractor;

  /**
   * A controller class responsible for handling user login requests.
   *
   * @param loginUseCaseInteractor The interactor responsible for executing the login use case.
   */
  public LoginController(LoginInputBoundary loginUseCaseInteractor) {
    this.loginUseCaseInteractor = loginUseCaseInteractor;
  }

  /**
   * Attempts to log in with the given email and password.
   *
   * @param email    The email used for login.
   * @param password The password used for login.
   */
  public void execute(String email, String password) {
    LoginInputData loginInputData = new LoginInputData(email, password);
    loginUseCaseInteractor.execute(loginInputData);
  }
}
