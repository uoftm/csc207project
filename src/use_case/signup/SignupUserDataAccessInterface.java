package use_case.signup;

import entities.user_entities.User;
import java.util.Optional;

public interface SignupUserDataAccessInterface {
  boolean existsByName(String identifier);

  Optional<String> save(User user);
}
