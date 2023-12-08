package interface_adapter.rooms;

import use_case.rooms.LoadRoomsInputBoundary;

/**
 * The LoadRoomsController class is responsible for handling the automatic room loading queries
 */
public class LoadRoomsController {
  final LoadRoomsInputBoundary loadRooms;

  /**
   * The LoadRoomsController class is responsible for loading rooms.
   *
   * @param roomsUseCaseInteractor The input boundary to use to load rooms.
   */
  public LoadRoomsController(LoadRoomsInputBoundary roomsUseCaseInteractor) {
    this.loadRooms = roomsUseCaseInteractor;
  }

  /**
   * Loads the rooms through the loadRooms interactor.
   */
  public void loadRooms() {
    loadRooms.loadRooms();
  }
}
