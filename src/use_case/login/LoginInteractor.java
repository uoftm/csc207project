package use_case.login;

import entities.auth.User;
import entities.rooms.Room;
import java.util.List;

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
      List<Room> availableRooms = userDataAccessObject.getAvailableRooms(user);
      LoginOutputData loginOutputData = new LoginOutputData(user, availableRooms, true);
      loginPresenter.prepareSuccessView(loginOutputData);
    } catch (RuntimeException e) {
      loginPresenter.prepareFailView(e.getMessage());
    }
  }
}
