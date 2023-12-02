package entities.search;

public class SearchRequest {

  private final String queryRequest;

  private final String roomID;

  public SearchRequest(String queryRequest, String roomID) {
    this.queryRequest = queryRequest;
    this.roomID = roomID;
  }

  public String getQueryRequest() {
    return queryRequest;
  }

  public String getRoomID() {
    return roomID;
  }
}
