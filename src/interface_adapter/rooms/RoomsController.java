package interface_adapter.rooms;

import entities.Room;
import entities.user_entities.User;
import use_case.rooms.RoomsInputBoundary;
import use_case.rooms.RoomsInputData;

public class RoomsController {
  final RoomsInputBoundary roomsUseCaseInteractor;

  public RoomsController(RoomsInputBoundary roomsUseCaseInteractor) {
    this.roomsUseCaseInteractor = roomsUseCaseInteractor;
  }

  public void loadMessages(Room room, User user) {
    RoomsInputData roomsInputData = new RoomsInputData(room, user, null);
    // Example request to load messages
    roomsUseCaseInteractor.loadMessages(roomsInputData);
  }

  public void sendMessage(Room room, User user, String message) {
    RoomsInputData roomsInputData = new RoomsInputData(room, user, message);
    // Example request to send message
    roomsUseCaseInteractor.sendMessage(roomsInputData);
  }
}
