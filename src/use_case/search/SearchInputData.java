package use_case.search;

import java.time.Instant;

public class SearchInputData {
  private final Instant time;
  private final String roomID;
  private final String message;
  private final String authorId;

  public SearchInputData(Instant time, String roomID, String message, String authorId) {
    this.message = message;
    this.time = time;
    this.roomID = roomID;
    this.authorId = authorId;
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

  public String getAuthorID() {
    return authorId;
  }
}
