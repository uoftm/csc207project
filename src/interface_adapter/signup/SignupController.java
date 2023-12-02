package interface_adapter.signup;

import use_case.signup.SignupInputBoundary;
import use_case.signup.SignupInputData;

public class SignupController {

  final SignupInputBoundary userSignupUseCaseInteractor;

  public SignupController(SignupInputBoundary userSignupUseCaseInteractor) {
    this.userSignupUseCaseInteractor = userSignupUseCaseInteractor;
  }

  /**
   * Signup with the given input data.
   *
   * @param email The email address of the user.
   * @param username The username of the user.
   * @param password1 The password for the user.
   * @param password2 The second password for the user. (to check that they match)
   */
  public void execute(String email, String username, String password1, String password2) {
    SignupInputData signupInputData = new SignupInputData(email, username, password1, password2);

    userSignupUseCaseInteractor.execute(signupInputData);
  }
}
