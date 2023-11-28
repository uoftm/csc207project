package use_case.login;

import entity.User;
import java.util.Optional;

public class LoginInteractor implements LoginInputBoundary {
  final LoginUserDataAccessInterface userDataAccessObject;
  final LoginOutputBoundary loginPresenter;

  public LoginInteractor(
          LoginUserDataAccessInterface userDataAccessInterface,
          LoginOutputBoundary loginOutputBoundary) {
    this.userDataAccessObject = userDataAccessInterface;
    this.loginPresenter = loginOutputBoundary;
  }

  @Override
  public void execute(LoginInputData loginInputData) {
    String email = loginInputData.getEmail();
    String password = loginInputData.getPassword();

    Optional<User> optionalUser = userDataAccessObject.get(email, password);

    if (optionalUser.isEmpty()) {
      loginPresenter.prepareFailView(email + ": Account does not exist.");
      return;
    }

    User user = optionalUser.get();

    if (!password.equals(user.getPassword())) {
      loginPresenter.prepareFailView("Incorrect password for " + email + ".");
    } else {
      LoginOutputData loginOutputData = new LoginOutputData(user.getName(), true);
      loginPresenter.prepareSuccessView(loginOutputData);
    }
  }
}
