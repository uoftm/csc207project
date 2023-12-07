package use_case.rooms;

import entities.auth.User;
import entities.rooms.Room;

public class RoomsOutputData {
  private final Room room;
  private final String error;
  private final String success;

  public RoomsOutputData(Room room, String error, String success) {
    this.room = room;
    this.error = error;
    this.success = success;
  }

  public Room getRoom() {
    return room;
  }

  public String getError() {
    return error;
  }

  public String getSuccess() {
    return success;
  }
}
