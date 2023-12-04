package use_case.rooms;

import entities.auth.User;

public class LoadRoomsInputData {
  User user;

  public LoadRoomsInputData(User user) {
    this.user = user;
  }

  User getUser() {
    return user;
  }
}
