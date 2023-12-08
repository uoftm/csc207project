package entities.search;

import java.time.Instant;

public class SearchChatMessage {
  private final String content;
  private final Instant timestamp;
  private final String email;
  private final String roomUid;

  /** Represents a search chat message. This is a message stored in Elasticsearch for indexing */
  public SearchChatMessage(Instant time, String roomUid, String message, String email) {
    this.timestamp = time;
    this.content = message;
    this.email = email;
    this.roomUid = roomUid;
  }

  public Instant getTime() {
    return this.timestamp;
  }

  public String getMessage() {
    return this.content;
  }

  public String getRoomUid() {
    return roomUid;
  }

  public String getAuthorEmail() {
    return this.email;
  }
}
