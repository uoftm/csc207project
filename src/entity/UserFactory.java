package entity;

import java.time.LocalDateTime;

public interface UserFactory {
  /** Requires: password is valid. */
  User create(String email, String name, String password, LocalDateTime ltd);
}
