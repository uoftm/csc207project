package use_case.search;

import entities.search.SearchChatMessage;
import entities.search.SearchRequest;
import use_case.rooms.LoggedInDataAccessInterface;

public class SearchInteractor implements SearchInputBoundary {

  final SearchDataAccessInterface searchDataAccessObject;

  final SearchOutputBoundary searchPresenter;
  private final LoggedInDataAccessInterface inMemoryDAO;

  public SearchInteractor(
          SearchDataAccessInterface searchDataAccessObject, LoggedInDataAccessInterface inMemoryDAO, SearchOutputBoundary searchOutputBoundary) {
    this.searchDataAccessObject = searchDataAccessObject;
    this.searchPresenter = searchOutputBoundary;
    this.inMemoryDAO = inMemoryDAO;
  }

  @Override
  public void executeSearchRequest(SearchInputData searchInputData) {
    SearchRequest request =
        new SearchRequest(searchInputData.getMessage(), searchInputData.getRoomUid());
    SearchOutputData outputData = new SearchOutputData(searchDataAccessObject.getData(request));
    if (outputData.getResponse().getIsError()) {
      searchPresenter.prepareFailedResponse(outputData);
    } else {
      searchPresenter.prepareSearchResponse(outputData);
    }
  }

  @Override
  public void executeRecordData(SearchInputData searchInputData) {
    String authorUid = inMemoryDAO.getUser().getUid();
    SearchChatMessage chatMessage =
        new SearchChatMessage(
            searchInputData.getTime(),
            searchInputData.getRoomUid(),
            searchInputData.getMessage(),
                authorUid);
    searchDataAccessObject.saveData(chatMessage);
  }
}
