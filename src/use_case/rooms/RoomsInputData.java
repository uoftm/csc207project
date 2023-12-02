package use_case.rooms;

import entities.auth.User;
import entities.rooms.Room;

public class RoomsInputData {
  Room room;
  User user;
  String message; // Nullable field
  String email;  // Nullable field
  String createRoom;  // Nullable field

  public RoomsInputData(Room room, User user, String message, String email, String createRoom) {
    this.room = room;
    this.user = user;
    this.message = message;
    this.email = email;
    this.createRoom = createRoom;
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
    return email;
  }

  String getCreateRoom() { return createRoom; }
}
