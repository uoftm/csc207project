package use_case.login;

import entities.auth.User;
import java.util.List;

public interface LoginUserDataAccessInterface {
  User getUser(String email, String password);

  String getAccessToken(String email, String password);

  List<String> getAvailableRoomIds(User user);
}
