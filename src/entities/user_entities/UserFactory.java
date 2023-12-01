package entities.user_entities;

import java.time.LocalDateTime;

public class UserFactory {
  public User create(String uid, String email, String name, String password, LocalDateTime ltd) {
    return new User(uid, email, name, password, ltd);
  }
}
