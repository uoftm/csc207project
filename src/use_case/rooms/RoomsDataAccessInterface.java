package use_case.rooms;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import use_case.login.LoginUserDataAccessInterface;

import java.util.List;

public interface RoomsDataAccessInterface {
  Room getRoomFromId(User user, LoginUserDataAccessInterface userDAO, String roomId);
  Room addRoom(User user, LoginUserDataAccessInterface userDAO, String roomName);
  void deleteRoom(User user, LoginUserDataAccessInterface userDAO, Room room);
}
