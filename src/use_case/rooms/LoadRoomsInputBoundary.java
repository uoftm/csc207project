package use_case.rooms;

import entities.rooms.Room;
import java.util.List;

public interface LoadRoomsInputBoundary {
  List<Room> loadRooms(LoadRoomsInputData roomsInputData);
}
