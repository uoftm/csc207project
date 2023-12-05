package use_case.room_settings;

import entities.auth.User;
import entities.rooms.Room;

public interface RoomSettingsInputBoundary {
  void deleteRoom(User user, Room activeRoom);

  void changeRoomName(User user, Room activeRoom, String newRoomName);
}
