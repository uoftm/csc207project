package use_case.rooms;

import entities.auth.User;
import entities.rooms.Room;

public class RoomsInputData {
  final Room room;
  final User user;
  final String message;
  final String userToAddEmail;
  final String roomToCreateName;

  public RoomsInputData(
      Room room, User user, String message, String userToAddEmail, String roomToCreateName) {
    this.room = room;
    this.user = user;
    this.message = message;
    this.userToAddEmail = userToAddEmail;
    this.roomToCreateName = roomToCreateName;
  }

  Room getRoom() {
    return room;
  }

  User getUser() {
    return user;
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
