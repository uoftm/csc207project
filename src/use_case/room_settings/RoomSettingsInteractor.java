package use_case.room_settings;

import entities.auth.User;
import entities.rooms.Room;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.LoggedInDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;

public class RoomSettingsInteractor implements RoomSettingsInputBoundary {
  private final RoomsDataAccessInterface roomsDataAccessObject;
  private final LoginUserDataAccessInterface userDataAccessObject;
  private final LoggedInDataAccessInterface inMemoryDAO;

  private final RoomSettingsOutputBoundary outputBoundary;

  public RoomSettingsInteractor(
      RoomsDataAccessInterface roomsDataAccessObject,
      LoginUserDataAccessInterface userDataAccessObject,
      LoggedInDataAccessInterface inMemoryDAO,
      RoomSettingsOutputBoundary outputBoundary) {
    this.roomsDataAccessObject = roomsDataAccessObject;
    this.userDataAccessObject = userDataAccessObject;
    this.outputBoundary = outputBoundary;
    this.inMemoryDAO = inMemoryDAO;
  }

  @Override
  public void deleteRoom(Room activeRoom) {
    try {
      User user = inMemoryDAO.getUser();
      roomsDataAccessObject.deleteRoom(user, userDataAccessObject, activeRoom);
      outputBoundary.prepareDeleteRoomSuccessView(new RoomSettingsOutputData(activeRoom));
    } catch (RuntimeException e) {
      outputBoundary.prepareDeleteRoomFailView(new RoomSettingsOutputData(e.getMessage()));
    }
  }

  @Override
  public void changeRoomName(Room activeRoom, String newRoomName) {
    try {
      User user = inMemoryDAO.getUser();
      roomsDataAccessObject.changeRoomName(user, userDataAccessObject, activeRoom, newRoomName);
      activeRoom.setName(newRoomName);
      outputBoundary.prepareChangeRoomNameSuccessView(new RoomSettingsOutputData(activeRoom));
    } catch (RuntimeException e) {
      outputBoundary.prepareChangeRoomFailView(new RoomSettingsOutputData(e.getMessage()));
    }
  }
}
