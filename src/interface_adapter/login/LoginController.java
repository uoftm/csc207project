package interface_adapter.login;

import use_case.login.LoginInputBoundary;
import use_case.login.LoginInputData;
import use_case.switch_view.SwitchViewInputBoundary;
import use_case.switch_view.SwitchViewInputData;

public class LoginController {

  final LoginInputBoundary loginUseCaseInteractor;
  private final SwitchViewInputBoundary switchViewInteractor;

  public LoginController(LoginInputBoundary loginUseCaseInteractor, SwitchViewInputBoundary switchViewInteractor) {
    this.loginUseCaseInteractor = loginUseCaseInteractor;
    this.switchViewInteractor = switchViewInteractor;
  }

  public void execute(String username, String password) {
    LoginInputData loginInputData = new LoginInputData(username, password);

    loginUseCaseInteractor.execute(loginInputData);
  }

  public void switchToSignup() {
    switchViewInteractor.execute(new SwitchViewInputData("sign up"));
  }
}
