package use_case.search;

import java.time.Instant;

public class SearchInputData {

  private final Instant time;
  private final String roomID;

  private final String message;

  SearchInputData(Instant time, String roomID, String message) {
    this.message = message;
    this.time = time;
    this.roomID = roomID;
  }

  public Instant getTime() {
    return time;
  }

  public String getRoomID() {
    return roomID;
  }

  public String getMessage() {
    return message;
  }
}
