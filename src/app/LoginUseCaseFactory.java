package app;

import entity.CommonUserFactory;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginPresenter;
import interface_adapter.login.LoginViewModel;
import java.io.IOException;
import javax.swing.*;
import use_case.login.LoginInputBoundary;
import use_case.login.LoginInteractor;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginUserDataAccessInterface;
import use_case.switch_view.SwitchViewInputBoundary;
import use_case.switch_view.SwitchViewInteractor;
import use_case.switch_view.SwitchViewOutputBoundary;
import use_case.switch_view.SwitchViewUserDataAccessInterface;
import view.LoginView;

public class LoginUseCaseFactory {

  /** Prevent instantiation. */
  private LoginUseCaseFactory() {}

  public static LoginView create(
          ViewManagerModel viewManagerModel,
          LoginViewModel loginViewModel,
          LoggedInViewModel loggedInViewModel,
          LoginUserDataAccessInterface userDataAccessObject) {

    try {
      LoginController loginController =
              createLoginUseCase(
                      viewManagerModel, loginViewModel, loggedInViewModel, userDataAccessObject);
      return new LoginView(loginViewModel, loginController);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Could not open user data file.");
    }

    return null;
  }

  private static LoginController createLoginUseCase(
          ViewManagerModel viewManagerModel,
          LoginViewModel loginViewModel,
          LoggedInViewModel loggedInViewModel,
          LoginUserDataAccessInterface userDataAccessObject)
          throws IOException {

    // Notice how we pass this method's parameters to the Presenter.
    LoginOutputBoundary loginOutputBoundary =
            new LoginPresenter(viewManagerModel, loggedInViewModel, loginViewModel);

    UserFactory userFactory = new CommonUserFactory();

    LoginInputBoundary loginInteractor =
            new LoginInteractor(userDataAccessObject, loginOutputBoundary);

    SwitchViewOutputBoundary switchViewOutputBoundary = (SwitchViewOutputBoundary) loginOutputBoundary;
    SwitchViewInputBoundary switchViewInteractor =
            new SwitchViewInteractor(switchViewOutputBoundary);

    return new LoginController(loginInteractor, switchViewInteractor);
  }
}
