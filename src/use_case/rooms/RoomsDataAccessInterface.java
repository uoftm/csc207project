package use_case.rooms;

import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Room;
import java.util.List;

public interface RoomsDataAccessInterface {
  Room getRoomFromId(String idToken, User user, String roomId);

  Room addRoom(String idToken, User user, String roomName);

  void deleteRoom(String idToken, User user, Room room);

  void addUserToRoom(String idToken, User currentUser, DisplayUser newUser, Room room);

  List<String> getAvailableRoomIds(User user);

  void removeUserFromRoom(String idToken, User currentUser, DisplayUser userToRemove, Room room);

  void changeRoomName(String idToken, User user, Room activeRoom, String roomName);
}
