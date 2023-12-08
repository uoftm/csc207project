package use_case.rooms;

/**
 * The LoadRoomsInputBoundary interface represents the contract for loading rooms. It provides a
 * simple interface for the LoadRoomsController class to call
 */
public interface LoadRoomsInputBoundary {
  /** Loads the rooms */
  void loadRooms();
}
