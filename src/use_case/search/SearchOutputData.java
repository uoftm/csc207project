package use_case.search;

public class SearchOutputData {
  private final String response;

  SearchOutputData(String response) {
    this.response = response;
  }

  public String getResponse() {
    return response;
  }
}
