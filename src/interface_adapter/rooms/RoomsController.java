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
    // Example request to load messages
    roomsUseCaseInteractor.loadMessages(roomsInputData);
  }

  public void sendMessage(Room room, User user, String message) {
    RoomsInputData roomsInputData = new RoomsInputData(room, user, message, null, null);
    // Example request to send message
    roomsUseCaseInteractor.sendMessage(roomsInputData);
  }

  public void addUserToRoom(Room room, User user, String email) {
    RoomsInputData roomsInputData = new RoomsInputData(room, user, null, email, null );
    // Example request to add user to room
    roomsUseCaseInteractor.addUserToRoom(roomsInputData);
  }

  public void createRoom(User user, String createRoom) {
    RoomsInputData roomsInputData = new RoomsInputData(null, user, null, null, createRoom );
    // Example request to create room
    roomsUseCaseInteractor.createRoom(roomsInputData);
  }
}
