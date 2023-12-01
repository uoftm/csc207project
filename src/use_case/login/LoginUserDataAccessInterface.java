package use_case.login;

import entities.Room;
import entities.user_entities.User;
import java.util.List;

public interface LoginUserDataAccessInterface {
  User get(String email, String password);

  List<Room> getAvailableRooms(User user);
}
