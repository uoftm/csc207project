package entities.search;

import java.time.Instant;

public class SearchResponse {
  private final String highlightedText;

  private final String fullText;
  private final String authorName;

  private final Instant time;

  private final String roomID;

  public SearchResponse(
      String highlightedText, String fullText, String authorName, Instant time, String roomID) {
    this.highlightedText = highlightedText;
    this.fullText = fullText;
    this.authorName = authorName;
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

  public String getAuthorName() {
    return authorName;
  }

  public String getRoomID() {
    return roomID;
  }
}
