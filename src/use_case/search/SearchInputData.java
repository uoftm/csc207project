package use_case.search;

import java.time.Instant;

public class SearchInputData {
  private final Instant time;
  private final String roomUid;
  private final String message;
  private final String authUid;

  public SearchInputData(Instant time, String roomUid, String message, String authUid) {
    this.message = message;
    this.time = time;
    this.roomUid = roomUid;
    this.authUid = authUid;
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

  public String getAuthUid() {
    return authUid;
  }
}
