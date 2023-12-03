package app;

import interface_adapter.rooms.RoomsController;
import interface_adapter.rooms.RoomsPresenter;
import interface_adapter.rooms.RoomsViewModel;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;
import use_case.rooms.RoomsInteractor;
import view.RoomsView;

public class RoomsUseCaseFactory {
  public static RoomsView create(
          RoomsDataAccessInterface roomsDataAccessObject, LoginUserDataAccessInterface userDao, RoomsViewModel roomsViewModel) {
    RoomsPresenter roomsPresenter = new RoomsPresenter(roomsViewModel);
    RoomsInteractor roomsInteractor = new RoomsInteractor(roomsDataAccessObject, userDao, roomsPresenter);
    RoomsController roomsController = new RoomsController(roomsInteractor);
    return new RoomsView(roomsViewModel, roomsController);
  }
}
