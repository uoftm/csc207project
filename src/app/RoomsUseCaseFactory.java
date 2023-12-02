package app;

import interface_adapter.rooms.RoomsController;
import interface_adapter.rooms.RoomsPresenter;
import interface_adapter.rooms.RoomsViewModel;
import use_case.rooms.RoomsDataAccessInterface;
import use_case.rooms.RoomsInteractor;
import view.RoomsView;

public class RoomsUseCaseFactory {
  /** Create the full Rooms use case factory, */
  public static RoomsView create(
      RoomsDataAccessInterface roomsDataAccessObject, RoomsViewModel roomsViewModel) {
    RoomsPresenter roomsPresenter = new RoomsPresenter(roomsViewModel);
    RoomsInteractor roomsInteractor = new RoomsInteractor(roomsDataAccessObject, roomsPresenter);
    RoomsController roomsController = new RoomsController(roomsInteractor);
    return new RoomsView(roomsViewModel, roomsController);
  }
}
