package entities.rooms;

import entities.auth.DisplayUser;
import java.time.Instant;

public class Message {
  private final String content;
  private final Instant timestamp;
  private final DisplayUser displayUser;

  public Message(Instant timestamp, String content, DisplayUser displayUser) {
    this.timestamp = timestamp;
    this.content = content;
    this.displayUser = displayUser;
  }

  public String getContent() {
    return content;
  }

  public Instant getTimestamp() {
    return timestamp;
  }

  public DisplayUser getDisplayUser() {
    return displayUser;
  }
}
