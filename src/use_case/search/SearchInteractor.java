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
    SearchRequest request =
        new SearchRequest(searchInputData.getMessage(), searchInputData.getRoomUid());
    SearchOutputData outputData = new SearchOutputData(searchDataAccessObject.getData(request));
    if (outputData.getResponse().getError() != null){
      searchPresenter.prepareFailedResponse(outputData);
    }else{
      searchPresenter.prepareSearchResponse(outputData);
    }

  }

  @Override
  public void executeRecordData(SearchInputData searchInputData) {
    SearchChatMessage chatMessage =
        new SearchChatMessage(
            searchInputData.getTime(),
            searchInputData.getRoomUid(),
            searchInputData.getMessage(),
            searchInputData.getAuthUid());
    searchDataAccessObject.saveData(chatMessage);
  }
}
