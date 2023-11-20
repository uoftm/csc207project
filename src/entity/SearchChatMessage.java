package entity;

import java.time.LocalDateTime;

public class SearchChatMessage implements ChatMessage {

  private final LocalDateTime messageTime;
  private final String roomID;
  private final String message;

  public SearchChatMessage(LocalDateTime time, String roomID, String message) {
    this.messageTime = time;
    this.roomID = roomID;
    this.message = message;
  }

  @Override
  public LocalDateTime getTime() {
    return messageTime;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public String getRoomID() {
    return roomID;
  }
}
