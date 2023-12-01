package interface_adapter.login;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.rooms.RoomsState;
import interface_adapter.rooms.RoomsViewModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;
import use_case.switch_view.SwitchViewOutputBoundary;
import use_case.switch_view.SwitchViewOutputData;

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

  @Override
  public void prepareSuccessView(LoginOutputData response) {
    LoggedInState loggedInState = loggedInViewModel.getState();
    loggedInState.setUsername(response.getUsername());
    this.loggedInViewModel.setState(loggedInState);
    this.loggedInViewModel.firePropertyChanged();

    RoomsState roomsState = roomsViewModel.getState();
    roomsState.setUserUid(response.getUid());
    roomsState.setAvailableRooms(response.getAvailableRooms());
    this.roomsViewModel.setState(roomsState);
    this.roomsViewModel.firePropertyChanged();

    this.viewManagerModel.setActiveView(loggedInViewModel.getViewName());
    this.viewManagerModel.firePropertyChanged();
  }

  @Override
  public void prepareFailView(String error) {
    LoginState loginState = loginViewModel.getState();
    loginState.setError(error);
    loginViewModel.firePropertyChanged();
  }

  @Override
  public void present(SwitchViewOutputData outputData) {
    this.viewManagerModel.setActiveView(outputData.getViewName());
    this.viewManagerModel.firePropertyChanged();
  }
}
