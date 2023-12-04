package entities.rooms;

import entities.auth.DisplayUser;

import java.time.Instant;

public class Message {
  public String content;
  public Instant timestamp;
  public DisplayUser displayUser;

  public Message(Instant timestamp, String content, DisplayUser displayUser) {
    this.timestamp = timestamp;
    this.content = content;
    this.displayUser = displayUser;
  }
}
