package use_case.rooms;

public interface RoomsInputBoundary {
  void loadMessages(RoomsInputData roomsInputData);

  void sendMessage(RoomsInputData roomsInputData);
}
