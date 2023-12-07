package use_case.room_settings;

import entities.rooms.Room;
import use_case.rooms.LoggedInDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;

public class RoomSettingsInteractor implements RoomSettingsInputBoundary {
  private final RoomsDataAccessInterface roomsDataAccessObject;
  private final LoggedInDataAccessInterface inMemoryDAO;

  private final RoomSettingsOutputBoundary outputBoundary;

  public RoomSettingsInteractor(
      RoomsDataAccessInterface roomsDataAccessObject,
      LoggedInDataAccessInterface inMemoryDAO,
      RoomSettingsOutputBoundary outputBoundary) {
    this.roomsDataAccessObject = roomsDataAccessObject;
    this.outputBoundary = outputBoundary;
    this.inMemoryDAO = inMemoryDAO;
  }

  @Override
  public void deleteRoom(Room activeRoom) {
    try {
      String idToken = inMemoryDAO.getIdToken();
      roomsDataAccessObject.deleteRoom(idToken, activeRoom);
      outputBoundary.prepareDeleteRoomSuccessView(new RoomSettingsOutputData(activeRoom));
    } catch (RuntimeException e) {
      outputBoundary.prepareDeleteRoomFailView(new RoomSettingsOutputData(e.getMessage()));
    }
  }

  @Override
  public void changeRoomName(Room activeRoom, String newRoomName) {
    try {
      String idToken = inMemoryDAO.getIdToken();
      roomsDataAccessObject.changeRoomName(idToken, activeRoom, newRoomName);
      activeRoom.setName(newRoomName);
      outputBoundary.prepareChangeRoomNameSuccessView(new RoomSettingsOutputData(activeRoom));
    } catch (RuntimeException e) {
      outputBoundary.prepareChangeRoomFailView(new RoomSettingsOutputData(e.getMessage()));
    }
  }
}
