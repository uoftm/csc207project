package interface_adapter.search;

import interface_adapter.ViewManagerModel;
import view.SearchView;

public class StartSearchController {
  private final ViewManagerModel viewManagerModel;
  private final SearchController searchController;
  private final SearchViewModel searchViewModel;

  public StartSearchController(
      ViewManagerModel viewManagerModel,
      SearchController searchController,
      SearchViewModel searchViewModel) {
    this.viewManagerModel = viewManagerModel;
    this.searchController = searchController;
    this.searchViewModel = searchViewModel;
  }

  public void search(String roomId, String userId) {
    searchViewModel.getState().setRoomID(roomId);
    searchViewModel.getState().setUserUid(userId);

    var searchView = new SearchView(searchController, searchViewModel);
    viewManagerModel.add(searchView.contentPane, "search");
    viewManagerModel.fireViewsChanged();
    viewManagerModel.setActiveView("search");
    viewManagerModel.fireViewChanged();
  }
}
