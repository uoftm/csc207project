package interface_adapter.search;

import interface_adapter.ViewManagerModel;

public class StartSearchController {
  private final ViewManagerModel viewManagerModel;
  private final SearchViewModel searchViewModel;

  public StartSearchController(ViewManagerModel viewManagerModel, SearchViewModel searchViewModel) {
    this.viewManagerModel = viewManagerModel;
    this.searchViewModel = searchViewModel;
  }

  public void search(String roomId, String userId) {
    searchViewModel.getState().setRoomUid(roomId);
    searchViewModel.getState().setUserUid(userId);

    viewManagerModel.setActiveView("search");
    viewManagerModel.fireViewChanged();
  }
}
