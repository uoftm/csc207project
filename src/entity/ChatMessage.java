package entity;

import java.time.LocalDateTime;

public interface ChatMessage {
  public LocalDateTime getTime();

  public String getMessage();
}
