package use_case.search;

import entities.search.SearchChatMessage;
import entities.search.SearchRequest;

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
    SearchRequest request = new SearchRequest(searchInputData.message(), searchInputData.roomUid());
    SearchOutputData outputData = new SearchOutputData(searchDataAccessObject.getData(request));
    if (outputData.getResponse().getIsError()) {
      searchPresenter.prepareFailedResponse(outputData);
    } else {
      searchPresenter.prepareSearchResponse(outputData);
    }
  }

  @Override
  public void executeRecordData(SearchInputData searchInputData) {
    SearchChatMessage chatMessage =
        new SearchChatMessage(
            searchInputData.time(),
            searchInputData.roomUid(),
            searchInputData.message(),
            searchInputData.authUid());
    searchDataAccessObject.saveData(chatMessage);
  }
}
