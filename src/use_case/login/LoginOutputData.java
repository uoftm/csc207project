package use_case.login;

import entities.auth.User;
import entities.rooms.Room;
import java.util.List;

public class LoginOutputData {
  private final User user;
  private final List<Room> availableRooms;

  public LoginOutputData(User user, List<Room> availableRooms) {
    this.user = user;
    this.availableRooms = availableRooms;
  }

  public String getUsername() {
    return user.getName();
  }

  public String getUid() {
    return user.getUid();
  }

  public User getUser() {
    return user;
  }

  public List<Room> getAvailableRooms() {
    return availableRooms;
  }
}
