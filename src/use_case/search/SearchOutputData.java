package use_case.search;

import entities.SearchReponseArray;

public class SearchOutputData {
  private final SearchReponseArray response;

  SearchOutputData(SearchReponseArray response) {
    this.response = response;
  }

  public SearchReponseArray getResponse() {
    return response;
  }
}
