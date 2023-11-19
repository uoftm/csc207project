package entity;

import java.time.Instant;

public class Message {
  public String content;
  public Instant timestamp;

  public Message(Instant timestamp, String content) {
    this.timestamp = timestamp;
    this.content = content;
  }
}
