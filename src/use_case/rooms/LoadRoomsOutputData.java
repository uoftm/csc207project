package use_case.rooms;

import entities.rooms.Room;
import java.util.List;

public class LoadRoomsOutputData {
  List<Room> rooms;
  private final boolean useCaseFailed;
  private final String error; // null iff useCaseFailed is false

  public LoadRoomsOutputData(List<Room> rooms, boolean useCaseFailed, String error) {
    this.rooms = rooms;
    this.useCaseFailed = useCaseFailed;
    this.error = error;
  }

  public List<Room> getRooms() {
    return rooms;
  }

  public boolean getUseCaseFailed() {
    return useCaseFailed;
  }

  public String getError() {
    return error;
  }
}
