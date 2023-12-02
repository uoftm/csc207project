package use_case.login;

import entities.auth.User;
import entities.rooms.Room;
import java.util.List;

public interface LoginUserDataAccessInterface {
  User get(String email, String password);

  List<Room> getAvailableRooms(User user);
}
