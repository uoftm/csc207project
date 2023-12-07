package entities.search;

import java.time.Instant;

public class SearchChatMessage {
  public final String content;
  public final Instant timestamp;
  public final String email;
  private final String roomUid;

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
