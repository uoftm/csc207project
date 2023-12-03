package entities.search;

public class SearchRequest {

  private final String queryRequest;

  private final String roomUid;

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
