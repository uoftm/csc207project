package use_case.login;

import entity.User;

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

    User user = userDataAccessObject.get(email, password);

    if (user == null) {
      loginPresenter.prepareFailView(email + ": Account does not exist.");
      return;
    }

    if (!password.equals(user.getPassword())) {
      loginPresenter.prepareFailView("Incorrect password for " + email + ".");
    } else {
      LoginOutputData loginOutputData = new LoginOutputData(user.getName(), user.getUid(), true);
      loginPresenter.prepareSuccessView(loginOutputData);
    }
  }
}
