package use_case.room_settings;


public interface RoomSettingsOutputBoundary {
  void prepareDeleteRoomSuccessView(RoomSettingsOutputData outputData);

  void prepareChangeRoomFailView(RoomSettingsOutputData outputData);

  void prepareChangeRoomNameSuccessView(RoomSettingsOutputData outputData);

  void prepareDeleteRoomFailView(RoomSettingsOutputData outputData);
}
