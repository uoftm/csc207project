package entities.search;

import java.time.Instant;

public class SearchResponse {

  private final String fullText;
  private final String authorName;

  private final Instant time;

  private final String roomName;

  public SearchResponse(String fullText, String authorName, Instant time, String roomName) {
    this.fullText = fullText;
    this.authorName = authorName;
    this.time = time;
    this.roomName = roomName;
  }

  public String getFullText() {
    return fullText;
  }

  public Instant getTime() {
    return time;
  }

  public String getAuthorName() {
    return authorName;
  }

  public String getRoomName() {
    return roomName;
  }
}
