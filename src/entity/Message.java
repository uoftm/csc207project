package entity;

import java.time.Instant;

public class Message {
  public String content;
  public Instant timestamp;
  public String authorId;

  public Message(Instant timestamp, String content, String authorId) {
    this.timestamp = timestamp;
    this.content = content;
    this.authorId = authorId;
  }
}
