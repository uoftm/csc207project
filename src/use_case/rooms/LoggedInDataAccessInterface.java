package use_case.rooms;

import entities.auth.User;

public interface LoggedInDataAccessInterface {
  void setIdToken(String idToken);

  void setUser(User user);

  String getIdToken();

  User getUser();
}
