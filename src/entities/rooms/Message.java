package entities.rooms;

import java.time.Instant;

public class Message {
  public String content;
  public Instant timestamp;
  public String authorEmail;

  public Message(Instant timestamp, String content, String authorEmail) {
    this.timestamp = timestamp;
    this.content = content;
    this.authorEmail = authorEmail;
  }
}
