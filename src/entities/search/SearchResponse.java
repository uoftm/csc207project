package entities.search;

import java.time.Instant;
import java.util.List;

public class SearchResponse {
  private final String fullText;

  private final String authUid;

  private final Instant time;

  private final String roomUid;

  private final List<SearchIndices> highlightIndices;

  /**
   * The SearchResponse class represents a single response of search chat messages from Elasticsearch.
   */
  public SearchResponse(
      String fullText,
      String authUid,
      Instant time,
      String roomUid,
      List<SearchIndices> highlightIndices) {
    this.fullText = fullText;
    this.authUid = authUid;
    this.time = time;
    this.roomUid = roomUid;
    this.highlightIndices = highlightIndices;
  }

  public String getFullText() {
    return fullText;
  }

  public Instant getTime() {
    return time;
  }

  public String getAuthUid() {
    return authUid;
  }

  public String getRoomUid() {
    return roomUid;
  }

  public List<SearchIndices> getHighlightIndices() {
    return highlightIndices;
  }
}
