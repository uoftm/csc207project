package use_case.login;

import entities.auth.User;
import entities.rooms.Room;
import java.util.List;
import use_case.switch_view.SwitchViewOutputBoundary;
import view.LoggedInView;

public class LoginInteractor implements LoginInputBoundary {
  final LoginUserDataAccessInterface userDataAccessObject;
  final LoginOutputBoundary loginPresenter;
  final SwitchViewOutputBoundary switchViewPresenter;

  public LoginInteractor(
      LoginUserDataAccessInterface userDataAccessInterface,
      LoginOutputBoundary loginOutputBoundary,
      SwitchViewOutputBoundary switchViewPresenter) {
    this.userDataAccessObject = userDataAccessInterface;
    this.loginPresenter = loginOutputBoundary;
    this.switchViewPresenter = switchViewPresenter;
  }

  /**
   * Executes the login process, and if it throws an exception, display it with the presenter
   *
   * @param loginInputData the login input data containing the user's email and password
   */
  @Override
  public void execute(LoginInputData loginInputData) {
    String email = loginInputData.getEmail();
    String password = loginInputData.getPassword();
    try {
      User user = userDataAccessObject.get(email, password);
      List<Room> availableRooms = userDataAccessObject.getAvailableRooms(user);
      LoginOutputData loginOutputData = new LoginOutputData(user, availableRooms);
      loginPresenter.prepareSuccessView(loginOutputData);
      switchViewPresenter.present(LoggedInView.viewName);
    } catch (RuntimeException e) {
      loginPresenter.prepareFailView(e.getMessage());
    }
  }
}
