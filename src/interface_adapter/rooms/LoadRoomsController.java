package interface_adapter.rooms;

import use_case.rooms.LoadRoomsInputBoundary;

public class LoadRoomsController {
  final LoadRoomsInputBoundary loadRooms;

  public LoadRoomsController(LoadRoomsInputBoundary roomsUseCaseInteractor) {
    this.loadRooms = roomsUseCaseInteractor;
  }

  public void loadRooms() {
    loadRooms.loadRooms();
  }
}
