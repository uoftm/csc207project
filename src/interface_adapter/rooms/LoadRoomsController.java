package interface_adapter.rooms;

import entities.auth.User;
import use_case.rooms.LoadRoomsInputBoundary;
import use_case.rooms.LoadRoomsInputData;

public class LoadRoomsController {
  final LoadRoomsInputBoundary loadRooms;

  public LoadRoomsController(LoadRoomsInputBoundary roomsUseCaseInteractor) {
    this.loadRooms = roomsUseCaseInteractor;
  }

  public void loadRooms(User user) {
    LoadRoomsInputData roomsInputData = new LoadRoomsInputData(user);
    loadRooms.loadRooms(roomsInputData);
  }
}
