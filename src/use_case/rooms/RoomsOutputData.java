package use_case.rooms;

import entities.auth.User;
import entities.rooms.Room;

public class RoomsOutputData {
  private final User user;
  private final Room room;
  private final String error;
  private final String success;

  public RoomsOutputData(Room room, User user, String error, String success) {
    this.room = room;
    this.user = user;
    this.error = error;
    this.success = success;
  }

  public Room getRoom() {
    return room;
  }

  public User getUser() {
    return user;
  }

  public String getError() {
    return error;
  }

  public String getSuccess() {
    return success;
  }
}
