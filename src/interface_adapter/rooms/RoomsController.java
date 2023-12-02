package interface_adapter.rooms;

import entities.auth.User;
import entities.rooms.Room;
import use_case.rooms.RoomsInputBoundary;
import use_case.rooms.RoomsInputData;

public class RoomsController {
  final RoomsInputBoundary roomsUseCaseInteractor;

  public RoomsController(RoomsInputBoundary roomsUseCaseInteractor) {
    this.roomsUseCaseInteractor = roomsUseCaseInteractor;
  }

  public void loadMessages(Room room, User user) {
    RoomsInputData roomsInputData = new RoomsInputData(room, user, null, null, null);
    roomsUseCaseInteractor.loadMessages(roomsInputData);
  }

  public void sendMessage(Room room, User user, String message) {
    RoomsInputData roomsInputData = new RoomsInputData(room, user, message, null, null);
    roomsUseCaseInteractor.sendMessage(roomsInputData);
  }

  public void addUserToRoom(Room room, User user, String userToAddEmail) {
    RoomsInputData roomsInputData = new RoomsInputData(room, user, null, userToAddEmail, null);
    roomsUseCaseInteractor.addUserToRoom(roomsInputData);
  }

  public void createRoom(User user, String roomToCreateName) {
    RoomsInputData roomsInputData = new RoomsInputData(null, user, null, null, roomToCreateName);
    roomsUseCaseInteractor.createRoom(roomsInputData);
  }
}
