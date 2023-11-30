package entity;

import java.time.LocalDateTime;

public interface User {
  String getUid();

  String getEmail();

  String getName();

  String getPassword();

  LocalDateTime getCreationTime();
}
