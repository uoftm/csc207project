package interface_adapter.search;

import entities.search.SearchResponse;
import entities.search.SearchResponseThing;
import interface_adapter.ViewManagerModel;
import interface_adapter.searched.SearchedState;
import interface_adapter.searched.SearchedViewModel;
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
    ArrayList<SearchResponseThing> returned = new ArrayList<>();
    String openTag = "<em>";
    String closeTag = "</em>";
    for (SearchResponse response : responses.getResponse().getResponses()) {
      String label =
          response.getTime().toString()
              + ", "
              + response.getAuthorName()
              + ", "
              + response.getRoomName()
              + ": ";

      returned.add(
          new SearchResponseThing(label, response.getHighlightIndices(), response.getFullText()));
    }
    searchedState.setResponses(returned);
    this.searchedViewModel.setState(searchedState);
    this.searchedViewModel.firePropertyChanged();

    this.viewManagerModel.setActiveView(searchedViewModel.getViewName());
    this.viewManagerModel.fireViewChanged();
  }

  @Override
  public void prepareFailedResponse() {
    SearchState searchState = searchViewModel.getState();
    searchState.setHasError(true);
    searchViewModel.firePropertyChanged();
  }
}
