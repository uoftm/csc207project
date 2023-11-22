package entity;

import java.time.LocalDateTime;

public interface User {

  String getEmail();
  String getName();

  String getPassword();

  LocalDateTime getCreationTime();
}
