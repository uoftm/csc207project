package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.rooms.RoomsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import javax.swing.*;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.LoggedInDataAccessInterface;
import view.LoginView;

public class LoginUseCaseFactory {

  /** Prevent instantiation. */
  private LoginUseCaseFactory() {}

  /** Creates a LoginView along with all the clean architecture components required to run it. */
  public static LoginView create(
      ViewManagerModel viewManagerModel,
      LoginViewModel loginViewModel,
      LoggedInViewModel loggedInViewModel,
      RoomsViewModel roomsViewModel,
      LoginUserDataAccessInterface userDataAccessObject,
      LoggedInDataAccessInterface inMemoryDAO,
      SwitchViewController switchViewController) {

    LoginController loginController =
        createLoginController(
            viewManagerModel,
            loginViewModel,
            loggedInViewModel,
            roomsViewModel,
            userDataAccessObject,
            inMemoryDAO);

    return new LoginView(loginViewModel, loginController, switchViewController);
  }

  private static LoginController createLoginController(
      ViewManagerModel viewManagerModel,
      LoginViewModel loginViewModel,
      LoggedInViewModel loggedInViewModel,
      RoomsViewModel roomsViewModel,
      LoginUserDataAccessInterface userDataAccessObject,
      LoggedInDataAccessInterface inMemoryDAO) {

    LoginOutputBoundary loginOutputBoundary =
        new LoginPresenter(viewManagerModel, loggedInViewModel, roomsViewModel, loginViewModel);
    LoginInputBoundary loginInteractor =
        new LoginInteractor(userDataAccessObject, inMemoryDAO, loginOutputBoundary);

    return new LoginController(loginInteractor);
  }
}
