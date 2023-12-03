package entities.search;

import java.util.ArrayList;

public class SearchReponseArray {

  private final ArrayList<SearchResponse> responses;

  private final String error;

  public SearchReponseArray(ArrayList<SearchResponse> responses, String error) {
    this.responses = responses;
    this.error = error;
  }

  public String getError() {
    return error;
  }

  public ArrayList<SearchResponse> getResponses() {
    return responses;
  }
}
