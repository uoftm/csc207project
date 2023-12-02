package app;

import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import java.io.IOException;
import javax.swing.*;
import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInteractor;
import use_case.signup.SignupOutputBoundary;
import use_case.signup.SignupUserDataAccessInterface;
import view.SignupView;

public class SignupUseCaseFactory {

  /** Prevent instantiation. */
  private SignupUseCaseFactory() {}

  /**
   * Creates the Signup page and the use case for it
   *
   * @return The instance of the Signup page
   */
  public static SignupView create(
      LoginViewModel loginViewModel,
      SignupViewModel signupViewModel,
      SignupUserDataAccessInterface userDataAccessObject) {

    try {
      SignupController signupController =
          createUserSignupUseCase(signupViewModel, loginViewModel, userDataAccessObject);
      return new SignupView(
          signupController, signupViewModel, SwitchViewUseCaseFactory.getController());
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Could not open user data file.");
    }

    return null;
  }

  /** Create the signup use case */
  private static SignupController createUserSignupUseCase(
      SignupViewModel signupViewModel,
      LoginViewModel loginViewModel,
      SignupUserDataAccessInterface userDataAccessObject)
      throws IOException {

    // Notice how we pass this method's parameters to the Presenter.
    SignupOutputBoundary signupOutputBoundary =
        new SignupPresenter(
            signupViewModel, loginViewModel, SwitchViewUseCaseFactory.getPresenter());

    SignupInputBoundary userSignupInteractor =
        new SignupInteractor(userDataAccessObject, signupOutputBoundary);

    return new SignupController(userSignupInteractor);
  }
}
