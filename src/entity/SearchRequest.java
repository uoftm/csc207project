package entity;

public class SearchRequest {

  private final String queryRequest;

  private String queryResponse;

  public SearchRequest(String queryRequest) {
    this.queryRequest = queryRequest;
    this.queryResponse = "";
  }

  public String getQueryRequest() {
    return queryRequest;
  }

  public String getQueryResponse() {
    return queryResponse;
  }

  public void changeQueryResponse(String newResponse) {
    queryResponse = newResponse;
  }
}
