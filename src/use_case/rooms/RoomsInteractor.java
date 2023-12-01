package use_case.rooms;

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
    String roomUid = roomsInputData.getRoomUid();
    String userUid = roomsInputData.getUserUid();

    // Make request with uid here
    // and get error (can be null) back
    String error = null;
    boolean useCaseFailed = error != null;

    // This request should be validated through firebase
    // to check if this user belongs to this room that exists
    boolean valid = roomsDataAccessObject.validateRoomAccess(roomUid, userUid);

    if (valid) {
      RoomsOutputData roomsOutputData = new RoomsOutputData(roomUid, valid, null);
      roomsPresenter.prepareFailView(roomsOutputData);
    } else {
      RoomsOutputData roomsOutputData = new RoomsOutputData(roomUid, valid, "Error here");
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }

  // TODO: Write more interactions like this
  @Override
  public void sendMessage(RoomsInputData roomsInputData) {
    String roomUid = roomsInputData.getRoomUid();
    String userUid = roomsInputData.getUserUid();
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
      RoomsOutputData roomsOutputData = new RoomsOutputData(roomUid, valid, null);
      roomsPresenter.prepareFailView(roomsOutputData);
    } else {
      RoomsOutputData roomsOutputData = new RoomsOutputData(roomUid, valid, "Error here");
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }
}
