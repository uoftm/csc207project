package use_case.rooms;

public interface RoomsInputBoundary {
  void sendMessage(RoomsInputData roomsInputData);

  void addUserToRoom(RoomsInputData roomsInputData);

  void createRoom(RoomsInputData roomsInputData);
}
