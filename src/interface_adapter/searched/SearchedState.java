package interface_adapter.searched;

import entity.SearchReponseArray;

public class SearchedState {

  private SearchReponseArray responses;

  public SearchedState(SearchReponseArray responses) {
    this.responses = responses;
  }

  public SearchedState() {}

  public void setResponses(SearchReponseArray responses) {
    this.responses = responses;
  }

  public SearchReponseArray getResponses() {
    return responses;
  }
}
