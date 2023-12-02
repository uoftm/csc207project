package interface_adapter.signup;

import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;
import use_case.switch_view.SwitchViewOutputBoundary;
import view.LoginView;

public class SignupPresenter implements SignupOutputBoundary {

  private final SignupViewModel signupViewModel;
  private final LoginViewModel loginViewModel;
  private final SwitchViewOutputBoundary switchViewOutputBoundary;

  public SignupPresenter(
      SignupViewModel signupViewModel,
      LoginViewModel loginViewModel,
      SwitchViewOutputBoundary switchViewOutputBoundary) {
    this.signupViewModel = signupViewModel;
    this.loginViewModel = loginViewModel;
    this.switchViewOutputBoundary = switchViewOutputBoundary;
  }

  /**
   * Prepares the view after a successful signup, switching to the login view. We could
   * automatically login here, but we choose to let the user login manually.
   *
   * @param response the response data from the signup operation
   */
  @Override
  public void prepareSuccessView(SignupOutputData response) {
    LoginState loginState = loginViewModel.getState();
    loginState.setEmail(response.getEmail());
    this.loginViewModel.setState(loginState);
    loginViewModel.firePropertyChanged();

    switchViewOutputBoundary.present(LoginView.viewName);
  }

  /**
   * Prepares the view after a failed signup, displaying a popup with the error message.
   *
   * @param error the error message to display
   */
  @Override
  public void prepareFailView(String error) {
    SignupState signupState = signupViewModel.getState();
    signupState.setError(error);
    signupViewModel.firePropertyChanged();
  }
}
