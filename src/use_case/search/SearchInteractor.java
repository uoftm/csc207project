package use_case.search;

import entity.SearchChatMessage;
import entity.SearchRequest;

public class SearchInteractor implements SearchInputBoundary {

  final SearchDataAccessInterface searchDataAccessObject;

  final SearchOutputBoundary searchPresenter;

  public SearchInteractor(
      SearchDataAccessInterface searchDataAccessObject, SearchOutputBoundary searchOutputBoundary) {
    this.searchDataAccessObject = searchDataAccessObject;
    this.searchPresenter = searchOutputBoundary;
  }

  @Override
  public void executeSearchRequest(SearchInputData searchInputData) {
    SearchRequest request =
        new SearchRequest(
            searchInputData.getMessage(), searchInputData.getTime(), searchInputData.getRoomID());
    SearchOutputData outputData = new SearchOutputData(searchDataAccessObject.getData(request));
    searchPresenter.prepareSearchResponse(outputData);
  }

  @Override
  public void executeRecordData(SearchInputData searchInputData) {
    SearchChatMessage chatMessage =
        new SearchChatMessage(
            searchInputData.getTime(),
            searchInputData.getRoomID(),
            searchInputData.getMessage(),
            searchInputData.getAuthorID());
    searchDataAccessObject.saveData(chatMessage);
  }
}
