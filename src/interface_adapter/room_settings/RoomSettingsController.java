package interface_adapter.room_settings;

import entities.rooms.Room;
import use_case.room_settings.RoomSettingsInputBoundary;

public class RoomSettingsController {
  private final RoomSettingsInputBoundary inputBoundary;

  public RoomSettingsController(RoomSettingsInputBoundary inputBoundary) {
    this.inputBoundary = inputBoundary;
  }

  public void saveRoomName(Room activeRoom, String roomName) {
    inputBoundary.changeRoomName(activeRoom, roomName);
  }

  /**
   * Delete the currently active room
   *
   * @param activeRoom The room that is currently active
   */
  public void deleteRoom(Room activeRoom) {
    inputBoundary.deleteRoom(activeRoom);
  }
}
