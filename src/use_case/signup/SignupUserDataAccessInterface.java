package use_case.signup;

import entities.auth.User;

public interface SignupUserDataAccessInterface {
  boolean existsByName(String identifier);

  void save(User user);
}
