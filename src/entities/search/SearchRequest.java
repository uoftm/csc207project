package entities.search;

/** The SearchRequest class represents a search request object. */
public class SearchRequest {

  private final String queryRequest;

  private final String roomUid;

  /**
   * Creates a new SearchRequest with the given query request and room UID.
   *
   * @param queryRequest The query for the search (as a raw phrase)
   * @param roomUid The room UID for the search.
   */
  public SearchRequest(String queryRequest, String roomUid) {
    this.queryRequest = queryRequest;
    this.roomUid = roomUid;
  }

  public String getQueryRequest() {
    return queryRequest;
  }

  public String getRoomUid() {
    return roomUid;
  }
}
