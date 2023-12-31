package use_case.rooms;

public interface RoomsOutputBoundary {
  void prepareSuccessView(RoomsOutputData outputData);

  void prepareSendMessageSuccessView(RoomsOutputData roomsOutputData);

  void prepareCreateRoomSuccessView(RoomsOutputData outputData);

  void prepareFailView(RoomsOutputData outputData);
}
