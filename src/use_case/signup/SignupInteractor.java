package use_case.signup;

import entities.auth.User;
import java.time.LocalDateTime;

public class SignupInteractor implements SignupInputBoundary {
  final SignupUserDataAccessInterface userDataAccessObject;
  final SignupOutputBoundary userPresenter;

  public SignupInteractor(
      SignupUserDataAccessInterface signupDataAccessInterface,
      SignupOutputBoundary signupOutputBoundary) {
    this.userDataAccessObject = signupDataAccessInterface;
    this.userPresenter = signupOutputBoundary;
  }

  @Override
  public void execute(SignupInputData signupInputData) {
    if (!signupInputData.getPassword().equals(signupInputData.getRepeatPassword())) {
      userPresenter.prepareFailView("Passwords don't match.");
    } else {
      LocalDateTime now = LocalDateTime.now();
      User user =
          new User(
              null,
              signupInputData.getEmail(),
              signupInputData.getUsername(),
              signupInputData.getPassword(),
              now);
      try {
        userDataAccessObject.save(user);
        SignupOutputData signupOutputData =
            new SignupOutputData(user.getEmail(), now.toString(), false);
        userPresenter.prepareSuccessView(signupOutputData);
      } catch (RuntimeException e) {
        userPresenter.prepareFailView(e.getMessage());
      }
    }
  }
}
