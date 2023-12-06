package use_case.room_settings;

import entities.auth.User;
import entities.rooms.Room;

public interface RoomSettingsInputBoundary {
  void deleteRoom(Room activeRoom);

  void changeRoomName(Room activeRoom, String newRoomName);
}
