package use_case.rooms;

import entities.auth.User;
import entities.rooms.Room;

public class RoomsInputData {
  Room room;
  User user;
  String message; // Nullable field

  public RoomsInputData(Room room, User user, String message) {
    this.room = room;
    this.user = user;
    this.message = message;
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
}
