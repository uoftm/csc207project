package use_case.rooms;

import entities.auth.User;
import entities.rooms.Room;
import java.util.ArrayList;
import java.util.List;

/**
 * The LoadRoomsInteractor class is responsible for loading rooms from the data source and sending them to the presenter
 */
public class LoadRoomsInteractor implements LoadRoomsInputBoundary {
  final LoadRoomsOutputBoundary roomsPresenter;
  final RoomsDataAccessInterface roomsDataAccessObject;
  final LoggedInDataAccessInterface inMemoryDAO;

  /**
   * The LoadRoomsInteractor class is responsible for loading rooms from the data source and sending them to the presenter
   *
   * @param roomsDataAccessObject The data access object to use to load rooms
   * @param inMemoryDAO The in memory data access object to use to get the logged in user and their id token
   * @param roomsOutputBoundary The output boundary to use to send the rooms to the presenter
   */
  public LoadRoomsInteractor(
      RoomsDataAccessInterface roomsDataAccessObject,
      LoggedInDataAccessInterface inMemoryDAO,
      LoadRoomsOutputBoundary roomsOutputBoundary) {
    this.roomsPresenter = roomsOutputBoundary;
    this.roomsDataAccessObject = roomsDataAccessObject;
    this.inMemoryDAO = inMemoryDAO;
  }

  @Override
  public void loadRooms() {
    try {
      inMemoryDAO.getUser();
      inMemoryDAO.getIdToken();
    } catch (RuntimeException e) {
      return;
    }
    try {
      User user = inMemoryDAO.getUser();
      List<String> availableRoomIds = roomsDataAccessObject.getAvailableRoomIds(user);
      List<Room> rooms = new ArrayList<>();
      for (String roomId : availableRoomIds) {
        String idToken = inMemoryDAO.getIdToken();
        Room room = roomsDataAccessObject.getRoomFromId(idToken, roomId);
        rooms.add(room);
      }
      LoadRoomsOutputData roomsOutputData = new LoadRoomsOutputData(rooms, null);
      roomsPresenter.prepareSuccessView(roomsOutputData);
    } catch (RuntimeException e) {
      LoadRoomsOutputData roomsOutputData = new LoadRoomsOutputData(null, e.getMessage());
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }
}
