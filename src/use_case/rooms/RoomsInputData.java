package use_case.rooms;

import entities.Room;
import entities.user_entities.User;

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
