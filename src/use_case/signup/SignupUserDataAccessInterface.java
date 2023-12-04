package use_case.signup;

import entities.auth.User;

public interface SignupUserDataAccessInterface {

  void save(User user);
}
