package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.search.SearchController;
import interface_adapter.search.SearchPresenter;
import interface_adapter.search.SearchViewModel;
import interface_adapter.searched.SearchedViewModel;
import use_case.rooms.LoggedInDataAccessInterface;
import use_case.search.SearchDataAccessInterface;
import use_case.search.SearchInputBoundary;
import use_case.search.SearchInteractor;
import use_case.search.SearchOutputBoundary;

public class SearchUseCaseFactory {

  private SearchUseCaseFactory() {}

  public static SearchController createSearchController(
      SearchViewModel searchViewModel,
      SearchDataAccessInterface searchDataAccessObject,
      LoggedInDataAccessInterface inMemoryDAO,
      ViewManagerModel viewManagerModel,
      SearchedViewModel searchedViewModel) {
    SearchOutputBoundary searchPresenter =
        new SearchPresenter(searchViewModel, searchedViewModel, viewManagerModel);

    SearchInputBoundary searchInteractor =
        new SearchInteractor(searchDataAccessObject, inMemoryDAO, searchPresenter);

    return new SearchController(searchInteractor);
  }
}
