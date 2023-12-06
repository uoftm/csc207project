package interface_adapter.room_settings;

import entities.auth.User;
import entities.rooms.Room;
import use_case.room_settings.RoomSettingsInputBoundary;

public class RoomSettingsController {
  private final RoomSettingsInputBoundary inputBoundary;

  public RoomSettingsController(RoomSettingsInputBoundary inputBoundary) {
    this.inputBoundary = inputBoundary;
  }

  public void saveRoomName(User user, Room activeRoom, String roomName) {
    inputBoundary.changeRoomName(user, activeRoom, roomName);
  }

  /**
   * Delete the currently active room
   *
   * @param user The user that is logged in
   * @param activeRoom The room that is currently active
   */
  public void deleteRoom(User user, Room activeRoom) {
    inputBoundary.deleteRoom(user, activeRoom);
  }
}
