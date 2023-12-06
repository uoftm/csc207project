package use_case.rooms;

import entities.auth.User;
import entities.rooms.Room;
import java.util.ArrayList;
import java.util.List;
import use_case.login.LoginUserDataAccessInterface;

public class LoadRoomsInteractor implements LoadRoomsInputBoundary {
  final LoadRoomsOutputBoundary roomsPresenter;
  final RoomsDataAccessInterface roomsDataAccessObject;
  final LoginUserDataAccessInterface userDao;
  final LoggedInDataAccessInterface inMemoryDAO;

  public LoadRoomsInteractor(
      RoomsDataAccessInterface roomsDataAccessObject,
      LoginUserDataAccessInterface userDao,
      LoggedInDataAccessInterface inMemoryDAO,
      LoadRoomsOutputBoundary roomsOutputBoundary) {
    this.roomsPresenter = roomsOutputBoundary;
    this.roomsDataAccessObject = roomsDataAccessObject;
    this.userDao = userDao;
    this.inMemoryDAO = inMemoryDAO;
  }

  @Override
  public void loadRooms() {
    try {
      User user = inMemoryDAO.getUser();
      List<String> availableRoomIds = roomsDataAccessObject.getAvailableRoomIds(user);
      List<Room> rooms = new ArrayList<>();
      for (String roomId : availableRoomIds) {
        Room room = roomsDataAccessObject.getRoomFromId(user, userDao, roomId);
        rooms.add(room);
      }
      LoadRoomsOutputData roomsOutputData = new LoadRoomsOutputData(rooms, false, null);
      roomsPresenter.prepareSuccessView(roomsOutputData);
    } catch (RuntimeException e) {
      LoadRoomsOutputData roomsOutputData = new LoadRoomsOutputData(null, true, e.getMessage());
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }
}
