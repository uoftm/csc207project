package app;

import interface_adapter.rooms.RoomsController;
import interface_adapter.rooms.RoomsPresenter;
import interface_adapter.rooms.RoomsViewModel;
import interface_adapter.search.StartSearchController;
import use_case.rooms.RoomsDataAccessInterface;
import use_case.rooms.RoomsInteractor;
import view.RoomsView;

public class RoomsUseCaseFactory {
  public static RoomsView create(
      RoomsDataAccessInterface roomsDataAccessObject,
      RoomsViewModel roomsViewModel,
      StartSearchController startSearchController) {
    RoomsPresenter roomsPresenter = new RoomsPresenter(roomsViewModel);
    RoomsInteractor roomsInteractor = new RoomsInteractor(roomsDataAccessObject, roomsPresenter);
    RoomsController roomsController = new RoomsController(roomsInteractor);
    return new RoomsView(roomsViewModel, roomsController, startSearchController);
  }
}
