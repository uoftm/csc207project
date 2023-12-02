package use_case.signup;

import entities.auth.User;
import entities.auth.UserFactory;
import java.time.LocalDateTime;

public class SignupInteractor implements SignupInputBoundary {
  final SignupUserDataAccessInterface userDataAccessObject;
  final SignupOutputBoundary userPresenter;
  final UserFactory userFactory;

  public SignupInteractor(
      SignupUserDataAccessInterface signupDataAccessInterface,
      SignupOutputBoundary signupOutputBoundary,
      UserFactory userFactory) {
    this.userDataAccessObject = signupDataAccessInterface;
    this.userPresenter = signupOutputBoundary;
    this.userFactory = userFactory;
  }

  @Override
  public void execute(SignupInputData signupInputData) {
    if (userDataAccessObject.existsByName(signupInputData.getEmail())) {
      userPresenter.prepareFailView(
          "A user has already signed up with this email, please enter another.");
    } else if (!signupInputData.getPassword().equals(signupInputData.getRepeatPassword())) {
      userPresenter.prepareFailView("Passwords don't match.");
    } else {
      LocalDateTime now = LocalDateTime.now();
      User user =
          userFactory.create(
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
