package use_case.rooms;

import entities.rooms.Room;
import java.util.List;

public class LoadRoomsOutputData {
  List<Room> rooms;
  private final String error;

  public LoadRoomsOutputData(List<Room> rooms, String error) {
    this.rooms = rooms;
    this.error = error;
  }

  public List<Room> getRooms() {
    return rooms;
  }

  public String getError() {
    return error;
  }
}
