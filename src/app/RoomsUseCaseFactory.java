package app;

import interface_adapter.room_settings.OpenRoomSettingsController;
import interface_adapter.rooms.*;
import interface_adapter.search.SearchController;
import interface_adapter.search.StartSearchController;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.LoadRoomsInteractor;
import use_case.rooms.MessageDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;
import use_case.rooms.RoomsInteractor;
import view.RoomsView;

public class RoomsUseCaseFactory {
  public static RoomsView create(
      RoomsDataAccessInterface roomsDataAccessObject,
      MessageDataAccessInterface messageDataAccessInterface,
      LoginUserDataAccessInterface userDao,
      RoomsViewModel roomsViewModel,
      SearchController searchController,
      StartSearchController startSearchController,
      OpenRoomSettingsController openRoomSettingsController) {
    RoomsPresenter roomsPresenter = new RoomsPresenter(roomsViewModel);
    RoomsInteractor roomsInteractor =
        new RoomsInteractor(
            roomsDataAccessObject, messageDataAccessInterface, userDao, roomsPresenter);
    RoomsController roomsController = new RoomsController(roomsInteractor);
    LoadRoomsPresenter loadRoomsPresenter = new LoadRoomsPresenter(roomsViewModel);
    LoadRoomsInteractor loadRoomsInteractor =
        new LoadRoomsInteractor(roomsDataAccessObject, userDao, loadRoomsPresenter);
    LoadRoomsController loadRoomsController = new LoadRoomsController(loadRoomsInteractor);
    return new RoomsView(
        roomsViewModel,
        roomsController,
        loadRoomsController,
        searchController,
        startSearchController,
        openRoomSettingsController);
  }
}
