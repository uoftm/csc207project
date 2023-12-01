package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.rooms.RoomsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.io.IOException;
import javax.swing.*;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginUserDataAccessInterface;
import view.LoginView;

public class LoginUseCaseFactory {

  /** Prevent instantiation. */
  private LoginUseCaseFactory() {}

  public static LoginView create(
      ViewManagerModel viewManagerModel,
      LoginViewModel loginViewModel,
      LoggedInViewModel loggedInViewModel,
      RoomsViewModel roomsViewModel,
      LoginUserDataAccessInterface userDataAccessObject,
      SwitchViewController switchViewController) {

    try {
      LoginController loginController =
          createLoginController(
              viewManagerModel,
              loginViewModel,
              loggedInViewModel,
              roomsViewModel,
              userDataAccessObject);

      return new LoginView(loginViewModel, loginController, switchViewController);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Could not open user data file.");
      return null;
    }
  }

  private static LoginController createLoginController(
      ViewManagerModel viewManagerModel,
      LoginViewModel loginViewModel,
      LoggedInViewModel loggedInViewModel,
      RoomsViewModel roomsViewModel,
      LoginUserDataAccessInterface userDataAccessObject)
      throws IOException {

    LoginOutputBoundary loginOutputBoundary =
        new LoginPresenter(viewManagerModel, loggedInViewModel, roomsViewModel, loginViewModel);
    LoginInputBoundary loginInteractor =
        new LoginInteractor(userDataAccessObject, loginOutputBoundary);

    return new LoginController(loginInteractor);
  }
}
