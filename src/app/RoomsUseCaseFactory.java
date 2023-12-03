package app;

import interface_adapter.rooms.*;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.LoadRoomsInteractor;
import use_case.rooms.RoomsDataAccessInterface;
import use_case.rooms.RoomsInteractor;
import view.RoomsView;

public class RoomsUseCaseFactory {
  public static RoomsView create(
          RoomsDataAccessInterface roomsDataAccessObject, LoginUserDataAccessInterface userDao, RoomsViewModel roomsViewModel) {
    RoomsPresenter roomsPresenter = new RoomsPresenter(roomsViewModel);
    RoomsInteractor roomsInteractor = new RoomsInteractor(roomsDataAccessObject, userDao, roomsPresenter);
    RoomsController roomsController = new RoomsController(roomsInteractor);
    LoadRoomsPresenter loadRoomsPresenter = new LoadRoomsPresenter(roomsViewModel);
    LoadRoomsInteractor loadRoomsInteractor = new LoadRoomsInteractor(roomsDataAccessObject, userDao, loadRoomsPresenter);
    LoadRoomsController loadRoomsController = new LoadRoomsController(loadRoomsInteractor);
    return new RoomsView(roomsViewModel, roomsController, loadRoomsController);
  }
}
