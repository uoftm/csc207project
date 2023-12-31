package use_case.rooms;

import entities.rooms.Room;

public class RoomsInputData {
  final Room room;
  final String message;
  final String userToAddEmail;
  final String roomToCreateName;

  public RoomsInputData(Room room, String message, String userToAddEmail, String roomToCreateName) {
    this.room = room;
    this.message = message;
    this.userToAddEmail = userToAddEmail;
    this.roomToCreateName = roomToCreateName;
  }

  Room getRoom() {
    return room;
  }

  String getMessage() {
    return message;
  }

  String getEmail() {
    return userToAddEmail;
  }

  String getRoomToCreateName() {
    return roomToCreateName;
  }
}
