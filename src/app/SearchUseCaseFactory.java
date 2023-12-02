package app;

import data_access.FirebaseUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.search.SearchController;
import interface_adapter.search.SearchPresenter;
import interface_adapter.search.SearchViewModel;
import interface_adapter.searched.SearchedViewModel;
import java.io.IOException;
import javax.swing.*;
import use_case.search.SearchDataAccessInterface;
import use_case.search.SearchInputBoundary;
import use_case.search.SearchInteractor;
import use_case.search.SearchOutputBoundary;
import view.SearchView;

public class SearchUseCaseFactory {

  private SearchUseCaseFactory() {}

  public static SearchView createSearchView(
      SearchViewModel searchViewModel,
      SearchDataAccessInterface searchDataAccessObject,
      ViewManagerModel viewManagerModel,
      SearchedViewModel searchedViewModel,
      String roomID,
      FirebaseUserDataAccessObject userDataAccessObject) {
    try {
      SearchController searchController =
          createSearchController(
              searchViewModel, searchDataAccessObject, viewManagerModel, searchedViewModel);
      return new SearchView(searchController, searchViewModel, roomID, userDataAccessObject);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Could not return search result.");
    }

    return null;
  }

  public static SearchController createSearchController(
      SearchViewModel searchViewModel,
      SearchDataAccessInterface searchDataAccessObject,
      ViewManagerModel viewManagerModel,
      SearchedViewModel searchedViewModel)
      throws IOException {
    SearchOutputBoundary searchPresenter =
        new SearchPresenter(searchViewModel, searchedViewModel, viewManagerModel);

    SearchInputBoundary searchInteractor =
        new SearchInteractor(searchDataAccessObject, searchPresenter);

    return new SearchController(searchInteractor);
  }
}
