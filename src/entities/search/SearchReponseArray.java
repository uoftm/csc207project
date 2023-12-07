package entities.search;

import java.util.ArrayList;

/**
 * Represents a response of messages from a search operation. Can also contain an error message if
 * the search failed.
 */
public class SearchReponseArray {

  private final ArrayList<SearchResponse> responses;

  private final String error;

  private final boolean isError;

  public SearchReponseArray(ArrayList<SearchResponse> responses, String error, Boolean isError) {
    this.responses = responses;
    this.error = error;
    this.isError = isError;
  }

  public String getError() {
    return error;
  }

  public boolean getIsError() {
    return isError;
  }

  public ArrayList<SearchResponse> getResponses() {
    return responses;
  }
}
