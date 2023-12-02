package entities.rooms;

import java.time.Instant;

public class Message {
  protected String content;
  protected Instant timestamp;
  protected String authorId;

  public Message(Instant timestamp, String content, String authorId) {
    this.timestamp = timestamp;
    this.content = content;
    this.authorId = authorId;
  }

  /**
   * @return the raw message content as a string
   */
  public String getContent() {
    return content;
  }

  /**
   * @return the Instant the message was sent
   */
  public Instant getTimestamp() {
    return timestamp;
  }

  /**
   * @return the uuid of the message author
   */
  public String getAuthorId() {
    return authorId;
  }
}
