package use_case.login;

import entities.Room;
import entities.user_entities.User;
import java.util.List;

public class LoginOutputData {
  private final User user;
  private final List<Room> availableRooms;
  private final boolean useCaseFailed;

  public LoginOutputData(User user, List<Room> availableRooms, boolean useCaseFailed) {
    this.user = user;
    this.availableRooms = availableRooms;
    this.useCaseFailed = useCaseFailed;
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
