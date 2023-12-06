package use_case.search;

import entities.search.SearchReponseArray;

public class SearchOutputData {
  private final SearchReponseArray response;

  public SearchOutputData(SearchReponseArray response) {
    this.response = response;
  }

  public SearchReponseArray getResponse() {
    return response;
  }
}
