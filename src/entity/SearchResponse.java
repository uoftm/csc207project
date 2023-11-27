package entity;

import java.time.Instant;

public class SearchResponse {
  private final String highlightedText;

  private final String fullText;

  private final Instant time;

  private final String roomID;

  public SearchResponse(String highlightedText, String fullText, Instant time, String roomID) {
    this.highlightedText = highlightedText;
    this.fullText = fullText;
    this.time = time;
    this.roomID = roomID;
  }

  public String getHighlightedText() {
    return highlightedText;
  }

  public String getFullText() {
    return fullText;
  }

  public Instant getTime() {
    return time;
  }

  public String getRoomID() {
    return roomID;
  }
}
