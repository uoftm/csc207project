package use_case.rooms;

import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Room;
import java.util.List;

public interface RoomsDataAccessInterface {
  Room getRoomFromId(String idToken, String roomId);

  Room addRoom(String idToken, User user, String roomName);

  void deleteRoom(String idToken, Room room);

  void addUserToRoom(String idToken, DisplayUser newUser, Room room);

  List<String> getAvailableRoomIds(User user);

  void removeUserFromRoom(String idToken, DisplayUser userToRemove, Room room);

  void changeRoomName(String idToken, Room activeRoom, String roomName);
}
