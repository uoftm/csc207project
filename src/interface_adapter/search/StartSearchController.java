package interface_adapter.search;

import interface_adapter.ViewManagerModel;
import view.SearchView;

public class StartSearchController {
  private final ViewManagerModel viewManagerModel;
  private final SearchViewModel searchViewModel;

  public StartSearchController(ViewManagerModel viewManagerModel, SearchViewModel searchViewModel) {
    this.viewManagerModel = viewManagerModel;
    this.searchViewModel = searchViewModel;
  }

  public void search(String roomId) {
    searchViewModel.getState().setRoomUid(roomId);

    viewManagerModel.setActiveView(SearchView.viewName);
  }
}
