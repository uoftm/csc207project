package entities.search;

import java.time.Instant;
import java.util.List;

public class SearchResponse {
  private final String fullText;

  private final String authorName;

  private final Instant time;

  private final String roomName;

  private final List<SearchIndicies> highlightIndices;

  public SearchResponse(
      String fullText,
      String authorName,
      Instant time,
      String roomName,
      List<SearchIndicies> highlightIndices) {
    this.fullText = fullText;
    this.authorName = authorName;
    this.time = time;
    this.roomName = roomName;
    this.highlightIndices = highlightIndices;
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

  public List<SearchIndicies> getHighlightIndices() {
    return highlightIndices;
  }
}
