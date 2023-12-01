package entities;

import java.time.Instant;

public class SearchRequest {

  private final String queryRequest;

  private final Instant time;

  private final String roomID;

  public SearchRequest(String queryRequest, Instant time, String roomID) {
    this.queryRequest = queryRequest;
    this.time = time;
    this.roomID = roomID;
  }

  public String getQueryRequest() {
    return queryRequest;
  }

  public Instant getTime() {
    return time;
  }

  public String getRoomID() {
    return roomID;
  }
}
