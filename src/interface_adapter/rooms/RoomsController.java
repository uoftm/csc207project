package interface_adapter.rooms;

import entities.rooms.Room;
import use_case.rooms.RoomsInputBoundary;
import use_case.rooms.RoomsInputData;

public class RoomsController {
  final RoomsInputBoundary roomsUseCaseInteractor;

  public RoomsController(RoomsInputBoundary roomsUseCaseInteractor) {
    this.roomsUseCaseInteractor = roomsUseCaseInteractor;
  }

  public void sendMessage(Room room, String message) {
    RoomsInputData roomsInputData = new RoomsInputData(room, message, null, null);
    roomsUseCaseInteractor.sendMessage(roomsInputData);
  }

  public void addUserToRoom(Room room, String userToAddEmail) {
    RoomsInputData roomsInputData = new RoomsInputData(room, null, userToAddEmail, null);
    roomsUseCaseInteractor.addUserToRoom(roomsInputData);
  }

  public void createRoom(String roomToCreateName) {
    RoomsInputData roomsInputData = new RoomsInputData(null, null, null, roomToCreateName);
    roomsUseCaseInteractor.createRoom(roomsInputData);
  }
}
