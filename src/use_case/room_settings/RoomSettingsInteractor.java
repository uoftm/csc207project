package use_case.room_settings;

import data_access.FirebaseUserDataAccessObject;
import entities.auth.User;
import entities.rooms.Room;
import use_case.rooms.RoomsDataAccessInterface;

public class RoomSettingsInteractor implements RoomSettingsInputBoundary {
  private final RoomsDataAccessInterface roomsDataAccessObject;
  private final FirebaseUserDataAccessObject userDataAccessObject;

  private final RoomSettingsOutputBoundary outputBoundary;

  public RoomSettingsInteractor(
      RoomsDataAccessInterface roomsDataAccessObject,
      FirebaseUserDataAccessObject userDataAccessObject,
      RoomSettingsOutputBoundary outputBoundary) {
    this.roomsDataAccessObject = roomsDataAccessObject;
    this.userDataAccessObject = userDataAccessObject;
    this.outputBoundary = outputBoundary;
  }

  @Override
  public void deleteRoom(User user, Room activeRoom) {
    roomsDataAccessObject.deleteRoom(user, userDataAccessObject, activeRoom);
    outputBoundary.deletedRoom(activeRoom);
  }

  @Override
  public void saveRoomName(User user, Room activeRoom, String roomName) {
    roomsDataAccessObject.changeRoomName(user, userDataAccessObject, activeRoom, roomName);
    outputBoundary.savedRoomName(activeRoom, roomName);
  }
}
