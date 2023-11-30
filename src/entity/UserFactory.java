package entity;

import java.time.LocalDateTime;

public interface UserFactory {
  /** Requires: password is valid. */
  User create(String uid, String email, String name, String password, LocalDateTime ltd);
}
