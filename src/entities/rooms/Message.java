package entities.rooms;

import entities.auth.DisplayUser;
import java.time.Instant;

public class Message {
  public final String content;
  public final Instant timestamp;
  public final DisplayUser displayUser;

  public Message(Instant timestamp, String content, DisplayUser displayUser) {
    this.timestamp = timestamp;
    this.content = content;
    this.displayUser = displayUser;
  }
}
