package use_case.search;

public interface SearchInputBoundary {
  void executeSearchRequest(SearchInputData searchInputData);

  void executeRecordData(SearchInputData searchInputData);
}
