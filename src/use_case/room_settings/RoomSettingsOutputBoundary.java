package use_case.room_settings;

import entities.rooms.Room;

public interface RoomSettingsOutputBoundary {
  void prepareDeleteRoomSuccessView(Room room);

  void prepareChangeRoomFailView(String message);

  void prepareChangeRoomNameSuccessView(Room activeRoom, String roomName);

  void prepareDeleteRoomFailView(String message);
}
