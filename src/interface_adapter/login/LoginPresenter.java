package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.rooms.RoomsViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;
import use_case.switch_view.SwitchViewOutputBoundary;
import use_case.switch_view.SwitchViewOutputData;
import view.LoggedInView;

public class LoginPresenter implements LoginOutputBoundary, SwitchViewOutputBoundary {

  private final LoginViewModel loginViewModel;
  private final LoggedInViewModel loggedInViewModel;
  private final RoomsViewModel roomsViewModel;
  private final ViewManagerModel viewManagerModel;

  public LoginPresenter(
      ViewManagerModel viewManagerModel,
      LoggedInViewModel loggedInViewModel,
      RoomsViewModel roomsViewModel,
      LoginViewModel loginViewModel) {
    this.viewManagerModel = viewManagerModel;
    this.loggedInViewModel = loggedInViewModel;
    this.roomsViewModel = roomsViewModel;
    this.loginViewModel = loginViewModel;
  }

  /**
   * Prepares the success view after a successful login. Sets the logged-in user, and updates the
   * view to display the Rooms view,
   *
   * @param loginOutputData The login output data containing the username of the logged-in user.
   */
  @Override
  public void prepareSuccessView(LoginOutputData loginOutputData) {
    this.loggedInViewModel.setLoggedInUser(loginOutputData.getUsername());
    this.loggedInViewModel.firePropertyChanged();
    this.roomsViewModel.firePropertyChanged();
    this.viewManagerModel.setActiveView(LoggedInView.viewName);
  }

  /**
   * Sets the error in the login state, which will be displayed as a popup
   *
   * @param error The error message to be displayed in the fail view.
   */
  @Override
  public void prepareFailView(String error) {
    LoginState loginState = loginViewModel.getState();
    loginState.setError(error);
    loginViewModel.firePropertyChanged();
  }

  /** Sets the active view in the view manager model based on the provided output data. */
  @Override
  public void present(SwitchViewOutputData outputData) {
    this.viewManagerModel.setActiveView(outputData.getViewName());
  }
}
