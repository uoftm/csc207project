package use_case.room_settings;

import entities.auth.User;
import entities.rooms.Room;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;

public class RoomSettingsInteractor implements RoomSettingsInputBoundary {
  private final RoomsDataAccessInterface roomsDataAccessObject;
  private final LoginUserDataAccessInterface userDataAccessObject;

  private final RoomSettingsOutputBoundary outputBoundary;

  public RoomSettingsInteractor(
      RoomsDataAccessInterface roomsDataAccessObject,
      LoginUserDataAccessInterface userDataAccessObject,
      RoomSettingsOutputBoundary outputBoundary) {
    this.roomsDataAccessObject = roomsDataAccessObject;
    this.userDataAccessObject = userDataAccessObject;
    this.outputBoundary = outputBoundary;
  }

  @Override
  public void deleteRoom(User user, Room activeRoom) {
    try {
      roomsDataAccessObject.deleteRoom(user, userDataAccessObject, activeRoom);
      outputBoundary.prepareDeleteRoomSuccessView(activeRoom);
    } catch (RuntimeException e) {
      outputBoundary.prepareDeleteRoomFailView(e.getMessage());
    }
  }

  @Override
  public void changeRoomName(User user, Room activeRoom, String newRoomName) {
    try {
      roomsDataAccessObject.changeRoomName(user, userDataAccessObject, activeRoom, newRoomName);
      outputBoundary.prepareChangeRoomNameSuccessView(activeRoom, newRoomName);
    } catch (RuntimeException e) {
      outputBoundary.prepareChangeRoomFailView(e.getMessage());
    }
  }
}
