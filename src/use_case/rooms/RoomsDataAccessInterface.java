package use_case.rooms;

import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Room;
import java.util.List;
import use_case.login.LoginUserDataAccessInterface;

public interface RoomsDataAccessInterface {
  Room getRoomFromId(User user, LoginUserDataAccessInterface userDAO, String roomId);

  Room addRoom(User user, LoginUserDataAccessInterface userDAO, String roomName);

  void deleteRoom(User user, LoginUserDataAccessInterface userDAO, Room room);

  void addUserToRoom(
      User currentUser, DisplayUser newUser, LoginUserDataAccessInterface userDAO, Room room);

  List<String> getAvailableRoomIds(User user);

  void removeUserFromRoom(User currentUser, DisplayUser userToRemove, LoginUserDataAccessInterface userDAO, Room room);
}
