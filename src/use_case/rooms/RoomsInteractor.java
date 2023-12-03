package use_case.rooms;

import entities.auth.User;
import entities.rooms.Room;

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

    // We will only know about rooms if we're authenticated to access them

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

    // Again, we will only know about rooms if we're authenticated to access them

    if (valid) {
      RoomsOutputData roomsOutputData = new RoomsOutputData(room, user, valid, null);
      roomsPresenter.prepareFailView(roomsOutputData);
    } else {
      RoomsOutputData roomsOutputData = new RoomsOutputData(room, user, valid, "Error here");
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }
}
