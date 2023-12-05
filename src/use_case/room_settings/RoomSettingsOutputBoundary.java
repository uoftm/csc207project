package use_case.room_settings;

import entities.rooms.Room;

public interface RoomSettingsOutputBoundary {
  void deletedRoom(Room room);

  void savedRoomName(Room activeRoom, String roomName);
}
