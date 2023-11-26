package entity;

import java.time.Instant;

public class SearchChatMessage extends Message {

  private final String roomID;

  public SearchChatMessage(Instant time, String roomID, String message) {
    super(time, message);
    this.roomID = roomID;
  }

  public Instant getTime() {
    return super.timestamp;
  }

  public String getMessage() {
    return super.content;
  }

  public String getRoomID() {
    return roomID;
  }
}
