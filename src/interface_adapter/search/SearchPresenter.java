package interface_adapter.search;

import interface_adapter.ViewManagerModel;
import interface_adapter.searched.SearchedState;
import interface_adapter.searched.SearchedViewModel;
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
    searchedState.setResponses(responses.getResponse());
    this.searchedViewModel.setState(searchedState);
    this.searchedViewModel.firePropertyChanged();
    this.viewManagerModel.setActiveView(searchedViewModel.getViewName());
    this.viewManagerModel.firePropertyChanged();
  }

  @Override
  public void prepareFailedResponse() {
    SearchState searchState = searchViewModel.getState();
    searchState.setHasError(true);
    searchViewModel.firePropertyChanged();
  }
}
