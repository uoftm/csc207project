package entities.search;

import java.util.ArrayList;

public class SearchReponseArray {

  private final ArrayList<SearchResponse> responses;

  private final Boolean hasError;

  public SearchReponseArray(ArrayList<SearchResponse> responses, Boolean hasError) {
    this.responses = responses;
    this.hasError = hasError;
  }

  public Boolean getHasError() {
    return hasError;
  }

  public ArrayList<SearchResponse> getResponses() {
    return responses;
  }
}
