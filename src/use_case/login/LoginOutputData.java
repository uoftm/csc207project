package use_case.login;

import entity.Room;
import java.util.List;

public class LoginOutputData {
  private final String username;
  private final String userUid;
  private final List<Room> availableRooms;
  private final boolean useCaseFailed;

  public LoginOutputData(
      String username, String userUid, List<Room> availableRooms, boolean useCaseFailed) {
    this.username = username;
    this.userUid = userUid;
    this.availableRooms = availableRooms;
    this.useCaseFailed = useCaseFailed;
  }

  public String getUsername() {
    return username;
  }

  public String getUid() {
    return userUid;
  }

  public List<Room> getAvailableRooms() {
    return availableRooms;
  }
}
