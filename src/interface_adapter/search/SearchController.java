package interface_adapter.search;

import java.time.Instant;
import use_case.search.SearchInputBoundary;
import use_case.search.SearchInputData;

public class SearchController {

  private final SearchInputBoundary searchInteractor;

  public SearchController(SearchInputBoundary searchInteractor) {
    this.searchInteractor = searchInteractor;
  }

  public void executeSearchRequest(String roomID, String message, String authID) {
    SearchInputData inputData = new SearchInputData(null, roomID, message, authID);
    searchInteractor.executeSearchRequest(inputData);
  }

  public void executeRecordData(Instant time, String roomID, String message, String authID) {
    SearchInputData inputData = new SearchInputData(time, roomID, message, authID);
    searchInteractor.executeRecordData(inputData);
  }
}
