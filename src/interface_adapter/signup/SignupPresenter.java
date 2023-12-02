package interface_adapter.signup;

import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

  @Override
  public void prepareSuccessView(SignupOutputData response) {
    // On success, switch to the login view.
    LocalDateTime responseTime = LocalDateTime.parse(response.getCreationTime());
    response.setCreationTime(responseTime.format(DateTimeFormatter.ofPattern("hh:mm:ss")));

    LoginState loginState = loginViewModel.getState();
    loginState.setEmail(response.getEmail());
    this.loginViewModel.setState(loginState);
    loginViewModel.firePropertyChanged();

    switchViewOutputBoundary.present(LoginView.viewName);
  }

  @Override
  public void prepareFailView(String error) {
    SignupState signupState = signupViewModel.getState();
    signupState.setError(error);
    signupViewModel.firePropertyChanged();
  }
}
