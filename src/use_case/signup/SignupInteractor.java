package use_case.signup;

import entities.auth.User;
import java.time.Instant;

public class SignupInteractor implements SignupInputBoundary {
  final SignupUserDataAccessInterface userDataAccessObject;
  final SignupOutputBoundary userPresenter;

  public SignupInteractor(
      SignupUserDataAccessInterface signupDataAccessInterface,
      SignupOutputBoundary signupOutputBoundary) {
    this.userDataAccessObject = signupDataAccessInterface;
    this.userPresenter = signupOutputBoundary;
  }

  /**
   * Executes the signup use case, by checking if the password and repeated password match, and if
   * the email is already in use. If the email is not in use, and the passwords match, the user is
   * saved through the SignupUserDataAccessInterface
   *
   * @param signupInputData the input data for the use case
   */
  @Override
  public void execute(SignupInputData signupInputData) {
    if (userDataAccessObject.existsByName(signupInputData.getEmail())) {
      userPresenter.prepareFailView(
          "A user has already signed up with this email, please enter another.");
    } else if (!signupInputData.getPassword().equals(signupInputData.getRepeatPassword())) {
      userPresenter.prepareFailView("Passwords don't match.");
    } else {
      Instant now = Instant.now();
      User user =
          new User(
              null,
              signupInputData.getEmail(),
              signupInputData.getUsername(),
              signupInputData.getPassword(),
              now);
      try {
        userDataAccessObject.save(user);
        SignupOutputData signupOutputData = new SignupOutputData(user.getEmail(), now);
        userPresenter.prepareSuccessView(signupOutputData);
      } catch (RuntimeException e) {
        userPresenter.prepareFailView(e.getMessage());
      }
    }
  }
}
