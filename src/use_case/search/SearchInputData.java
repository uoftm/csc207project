package use_case.search;

import java.time.LocalDateTime;

public class SearchInputData {

  private final LocalDateTime time;
  private final String roomID;

  private final String message;

  SearchInputData(String message, LocalDateTime time, String roomID) {
    this.message = message;
    this.time = time;
    this.roomID = roomID;
  }

  public LocalDateTime getTime() {
    return time;
  }

  public String getRoomID() {
    return roomID;
  }

  public String getMessage() {
    return message;
  }
}
