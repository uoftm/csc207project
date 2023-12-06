package use_case.login;

import entities.auth.User;
import use_case.rooms.LoggedInDataAccessInterface;

public class LoginInteractor implements LoginInputBoundary {
  final LoginUserDataAccessInterface userDataAccessObject;
  final LoginOutputBoundary loginPresenter;
  final LoggedInDataAccessInterface inMemoryDAO;

  public LoginInteractor(
      LoginUserDataAccessInterface userDataAccessInterface,
      LoggedInDataAccessInterface inMemoryDAO,
      LoginOutputBoundary loginOutputBoundary) {
    this.userDataAccessObject = userDataAccessInterface;
    this.loginPresenter = loginOutputBoundary;
    this.inMemoryDAO = inMemoryDAO;
  }

  @Override
  public void execute(LoginInputData loginInputData) {
    String email = loginInputData.getEmail();
    String password = loginInputData.getPassword();
    try {
      User user = userDataAccessObject.getUser(email, password);
      inMemoryDAO.setUser(user);
      LoginOutputData loginOutputData = new LoginOutputData(user.getName());
      loginPresenter.prepareSuccessView(loginOutputData);
    } catch (RuntimeException e) {
      loginPresenter.prepareFailView(e.getMessage());
    }
  }
}
