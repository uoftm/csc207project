package interface_adapter.search;

import entities.search.SearchResponse;
import entities.search.SearchResponseDisplay;
import interface_adapter.ViewManagerModel;
import interface_adapter.searched.SearchedState;
import interface_adapter.searched.SearchedViewModel;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import use_case.search.SearchOutputBoundary;
import use_case.search.SearchOutputData;

public class SearchPresenter implements SearchOutputBoundary {

  private final SearchViewModel searchViewModel;

  private final SearchedViewModel searchedViewModel;

  private final ViewManagerModel viewManagerModel;

  public SearchPresenter(
      SearchViewModel searchViewModel,
      SearchedViewModel searchedViewModel,
      ViewManagerModel viewManagerModel) {
    this.searchViewModel = searchViewModel;
    this.searchedViewModel = searchedViewModel;
    this.viewManagerModel = viewManagerModel;
  }

  @Override
  public void prepareSearchResponse(SearchOutputData responses) {
    SearchedState searchedState = searchedViewModel.getState();
    ArrayList<SearchResponseDisplay> returned = new ArrayList<>();
    for (SearchResponse response : responses.getResponse().getResponses()) {

      returned.add(
          new SearchResponseDisplay(
              DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss")
                  .format(LocalDateTime.ofInstant(response.time(), ZoneId.of("America/New_York"))),
              // TODO// needs to be modified to have names instead of IDs.
              response.authUid(),
              response.roomUid(),
              response.highlightIndices(),
              response.fullText()));
    }
    searchedState.setResponses(returned);
    this.searchedViewModel.setState(searchedState);
    this.searchedViewModel.firePropertyChanged();

    this.viewManagerModel.setActiveView(searchedViewModel.getViewName());
  }

  @Override
  public void prepareFailedResponse(SearchOutputData responses) {
    SearchState searchState = searchViewModel.getState();
    searchState.setError(responses.getResponse().getError());
    searchState.setIsError(responses.getResponse().getIsError());
    searchViewModel.firePropertyChanged();
  }
}
