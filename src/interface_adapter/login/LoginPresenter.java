package interface_adapter.login;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.rooms.RoomsState;
import interface_adapter.rooms.RoomsViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

public class LoginPresenter implements LoginOutputBoundary {
  private final LoginViewModel loginViewModel;
  private final LoggedInViewModel loggedInViewModel;
  private final RoomsViewModel roomsViewModel;

  public LoginPresenter(
      LoggedInViewModel loggedInViewModel,
      RoomsViewModel roomsViewModel,
      LoginViewModel loginViewModel) {
    this.loggedInViewModel = loggedInViewModel;
    this.roomsViewModel = roomsViewModel;
    this.loginViewModel = loginViewModel;
  }

  /**
   * Prepares the success view after a successful login by updating the state of the
   * loggedInViewModel and nested roomsViewModel based on the new information we have about the
   * user.
   *
   * <p>Then, the interactor will switch to the logged in view
   *
   * @param response The LoginOutputData containing the relevant data after a successful login.
   */
  @Override
  public void prepareSuccessView(LoginOutputData response) {
    LoggedInState loggedInState = loggedInViewModel.getState();
    loggedInState.setUsername(response.getUsername());
    this.loggedInViewModel.setState(loggedInState);
    this.loggedInViewModel.firePropertyChanged();

    RoomsState roomsState = roomsViewModel.getState();
    roomsState.setUser(response.getUser());
    roomsState.setAvailableRooms(response.getAvailableRooms());
    this.roomsViewModel.setState(roomsState);
    this.roomsViewModel.firePropertyChanged();
  }

  /**
   * If the user fails to log in, display an error message
   *
   * <p>This method sets the error message in the login state object and notifies the view model
   * that a property has changed, triggering the view to display the error message popup.
   *
   * @param error the error message to be displayed
   */
  @Override
  public void prepareFailView(String error) {
    LoginState loginState = loginViewModel.getState();
    loginState.setError(error);
    loginViewModel.firePropertyChanged();
  }
}
