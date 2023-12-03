package entities.search;

import entities.rooms.Message;
import java.time.Instant;

public class SearchChatMessage extends Message {

  private final String roomUid;

  public SearchChatMessage(Instant time, String roomUid, String message, String authorId) {
    super(time, message, authorId);
    this.roomUid = roomUid;
  }

  public Instant getTime() {
    return super.timestamp;
  }

  public String getMessage() {
    return super.content;
  }

  public String getRoomUid() {
    return roomUid;
  }

  public String getAuthorEmail() {
    return super.authorEmail;
  }
}
