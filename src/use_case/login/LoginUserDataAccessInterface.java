package use_case.login;

import entity.User;
import java.util.Optional;

public interface LoginUserDataAccessInterface {
  boolean existsByName(String identifier);

  Optional<String> save(User user);

  Optional<User> get(String email, String password);
}
