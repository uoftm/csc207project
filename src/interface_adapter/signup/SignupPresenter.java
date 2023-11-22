package interface_adapter.signup;

import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupOutputData;

public class SignupPresenter implements SignupOutputBoundary {

  private final SignupViewModel signupViewModel;
  private final LoginViewModel loginViewModel;
  private final ViewManagerModel viewManagerModel;

  public SignupPresenter(
      ViewManagerModel viewManagerModel,
      SignupViewModel signupViewModel,
      LoginViewModel loginViewModel) {
    this.viewManagerModel = viewManagerModel;
    this.signupViewModel = signupViewModel;
    this.loginViewModel = loginViewModel;
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
    loginViewModel.firePropertyChanged();

    viewManagerModel.setActiveView(loginViewModel.getViewName());
    viewManagerModel.firePropertyChanged();
  }

  @Override
  public void prepareFailView(String error) {
    SignupState signupState = signupViewModel.getState();
    signupState.setError(error);
    signupViewModel.firePropertyChanged();
  }
}
