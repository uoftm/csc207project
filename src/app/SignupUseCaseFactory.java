package app;

import entity.CommonUserFactory;
import entity.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupPresenter;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.switch_view.SwitchViewController;
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

  public static SignupView create(
      ViewManagerModel viewManagerModel,
      LoginViewModel loginViewModel,
      SignupViewModel signupViewModel,
      SignupUserDataAccessInterface userDataAccessObject,
      SwitchViewController switchViewController) {

    try {
      SignupController signupController =
          createUserSignupUseCase(
              viewManagerModel, signupViewModel, loginViewModel, userDataAccessObject);
      return new SignupView(signupController, signupViewModel, switchViewController);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Could not open user data file.");
    }

    return null;
  }

  private static SignupController createUserSignupUseCase(
      ViewManagerModel viewManagerModel,
      SignupViewModel signupViewModel,
      LoginViewModel loginViewModel,
      SignupUserDataAccessInterface userDataAccessObject)
      throws IOException {

    // Notice how we pass this method's parameters to the Presenter.
    SignupOutputBoundary signupOutputBoundary =
        new SignupPresenter(viewManagerModel, signupViewModel, loginViewModel);

    UserFactory userFactory = new CommonUserFactory();

    SignupInputBoundary userSignupInteractor =
        new SignupInteractor(userDataAccessObject, signupOutputBoundary, userFactory);

    return new SignupController(userSignupInteractor);
  }
}
