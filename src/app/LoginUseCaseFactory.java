package app;

import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import interface_adapter.rooms.RoomsViewModel;
import java.io.IOException;
import javax.swing.*;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginUserDataAccessInterface;
import view.LoginView;

public class LoginUseCaseFactory {

  /** Creates a LoginView along with all the clean architecture components required to run it. */
  public static LoginView create(
      LoginViewModel loginViewModel,
      LoggedInViewModel loggedInViewModel,
      RoomsViewModel roomsViewModel,
      LoginUserDataAccessInterface userDataAccessObject) {

    try {
      LoginController loginController =
          createLoginController(
              loginViewModel, loggedInViewModel, roomsViewModel, userDataAccessObject);

      return new LoginView(
          loginViewModel, loginController, SwitchViewUseCaseFactory.getController());
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Could not open user data file.");
      return null;
    }
  }

  private static LoginController createLoginController(
      LoginViewModel loginViewModel,
      LoggedInViewModel loggedInViewModel,
      RoomsViewModel roomsViewModel,
      LoginUserDataAccessInterface userDataAccessObject)
      throws IOException {

    LoginOutputBoundary loginOutputBoundary =
        new LoginPresenter(loggedInViewModel, roomsViewModel, loginViewModel);
    LoginInputBoundary loginInteractor =
        new LoginInteractor(
            userDataAccessObject, loginOutputBoundary, SwitchViewUseCaseFactory.getPresenter());

    return new LoginController(loginInteractor);
  }
}
