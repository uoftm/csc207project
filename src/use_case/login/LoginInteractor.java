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
    try {
      User user = userDataAccessObject.get(email, password);
      LoginOutputData loginOutputData = new LoginOutputData(user.getName(), true);
      loginPresenter.prepareSuccessView(loginOutputData);
    } catch (RuntimeException e) {
      loginPresenter.prepareFailView(e.getMessage());
    }
  }
}
