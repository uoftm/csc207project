package interface_adapter.rooms;

import use_case.rooms.RoomsInputBoundary;
import use_case.rooms.RoomsInputData;

public class RoomsController {
  final RoomsInputBoundary roomsUseCaseInteractor;

  public RoomsController(RoomsInputBoundary roomsUseCaseInteractor) {
    this.roomsUseCaseInteractor = roomsUseCaseInteractor;
  }

  public void loadMessages(String roomUid, String userUid) {
    RoomsInputData roomsInputData = new RoomsInputData(roomUid, userUid, null);
    // Example request to load messages
    roomsUseCaseInteractor.loadMessages(roomsInputData);
  }

  public void sendMessage(String roomUid, String userUid, String message) {
    RoomsInputData roomsInputData = new RoomsInputData(roomUid, userUid, message);
    // Example request to send message
    roomsUseCaseInteractor.sendMessage(roomsInputData);
  }
}
