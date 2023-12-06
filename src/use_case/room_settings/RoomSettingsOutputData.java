package use_case.room_settings;

import entities.rooms.Room;

public class RoomSettingsOutputData {
  private final Room room;
  private final String message;

  public RoomSettingsOutputData(Room room) {
    this.room = room;
    this.message = null;
  }

  public RoomSettingsOutputData(String message) {
    this.room = null;
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public Room getRoom() {
    return room;
  }
}
