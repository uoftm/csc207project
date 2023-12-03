package use_case.rooms;

import entities.rooms.Room;
import java.util.ArrayList;
import java.util.List;
import use_case.login.LoginUserDataAccessInterface;

public class LoadRoomsInteractor implements LoadRoomsInputBoundary {
  final LoadRoomsOutputBoundary roomsPresenter;
  final RoomsDataAccessInterface roomsDataAccessObject;
  final LoginUserDataAccessInterface userDao;

  public LoadRoomsInteractor(
      RoomsDataAccessInterface roomsDataAccessObject,
      LoginUserDataAccessInterface userDao,
      LoadRoomsOutputBoundary roomsOutputBoundary) {
    this.roomsPresenter = roomsOutputBoundary;
    this.roomsDataAccessObject = roomsDataAccessObject;
    this.userDao = userDao;
  }

  @Override
  public List<Room> loadRooms(LoadRoomsInputData roomsInputData) {
    // TODO: Trigger this function from the view
    List<String> availableRoomIds = roomsDataAccessObject.getAvailableRoomIds(roomsInputData.user);
    List<Room> rooms = new ArrayList<>();
    for (String roomId : availableRoomIds) {
      Room room = roomsDataAccessObject.getRoomFromId(roomsInputData.user, userDao, roomId);
      rooms.add(room);
    }
    return rooms;
  }
}
