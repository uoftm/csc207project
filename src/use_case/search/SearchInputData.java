package use_case.search;

import java.time.Instant;

public class SearchInputData {
  private final Instant time;
  private final String roomUid;
  private final String message;

  public SearchInputData(Instant time, String roomUid, String message) {
    this.message = message;
    this.time = time;
    this.roomUid = roomUid;
  }

  public Instant getTime() {
    return time;
  }

  public String getRoomUid() {
    return roomUid;
  }

  public String getMessage() {
    return message;
  }
}
