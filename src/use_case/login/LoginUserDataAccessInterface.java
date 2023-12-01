package use_case.login;

import entity.Room;
import entity.User;

import java.util.List;

public interface LoginUserDataAccessInterface {
  User get(String email, String password);
  List<Room> getAvailableRooms(User user);
}
