package use_case.rooms;

import entities.Room;
import entities.user_entities.User;

public class RoomsInteractor implements RoomsInputBoundary {
  final RoomsOutputBoundary roomsPresenter;
  final RoomsDataAccessInterface roomsDataAccessObject;

  public RoomsInteractor(
      RoomsDataAccessInterface roomsDataAccessObject, RoomsOutputBoundary roomsOutputBoundary) {
    this.roomsPresenter = roomsOutputBoundary;
    this.roomsDataAccessObject = roomsDataAccessObject;
  }

  // TODO: Write more interactions like this
  @Override
  public void loadMessages(RoomsInputData roomsInputData) {
    Room room = roomsInputData.getRoom();
    User user = roomsInputData.getUser();

    String roomUid = room.getUid();
    String userUid = user.getUid();

    // Make request with uid here
    // and get error (can be null) back
    String error = null;
    boolean useCaseFailed = error != null;

    // This request should be validated through firebase
    // to check if this user belongs to this room that exists
    boolean valid = roomsDataAccessObject.validateRoomAccess(roomUid, userUid);

    if (valid) {
      RoomsOutputData roomsOutputData = new RoomsOutputData(room, user, valid, null);
      roomsPresenter.prepareFailView(roomsOutputData);
    } else {
      RoomsOutputData roomsOutputData = new RoomsOutputData(room, user, valid, "Error here");
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }

  // TODO: Write more interactions like this
  @Override
  public void sendMessage(RoomsInputData roomsInputData) {
    Room room = roomsInputData.getRoom();
    User user = roomsInputData.getUser();

    String roomUid = room.getUid();
    String userUid = user.getUid();
    String message = roomsInputData.getMessage();

    // Make request with uid here
    // and get error (can be null) back
    String error = null;
    boolean useCaseFailed = error != null;

    // Put your call here @Justus
    System.out.println(message);

    // This request should be validated through firebase
    // to check if this user belongs to this room that exists
    boolean valid = roomsDataAccessObject.validateRoomAccess(roomUid, userUid);

    if (valid) {
      RoomsOutputData roomsOutputData = new RoomsOutputData(room, user, valid, null);
      roomsPresenter.prepareFailView(roomsOutputData);
    } else {
      RoomsOutputData roomsOutputData = new RoomsOutputData(room, user, valid, "Error here");
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }
}
