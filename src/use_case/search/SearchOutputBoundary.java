package use_case.search;

public interface SearchOutputBoundary {

  void prepareSearchResponse(SearchOutputData response);

  void prepareFailedResponse(SearchOutputData responses);
}
