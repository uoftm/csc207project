package use_case.rooms;

import entities.auth.User;
import entities.rooms.Room;

public class RoomsOutputData {
  User user;
  Room room;
  private final boolean useCaseFailed;
  private final String error; // null iff useCaseFailed is false

  public RoomsOutputData(Room room, User user, boolean useCaseFailed, String error) {
    this.room = room;
    this.user = user;
    this.useCaseFailed = useCaseFailed;
    this.error = error;
  }

  public Room getRoom() {
    return room;
  }

  public User getUser() {
    return user;
  }

  public boolean getUseCaseFailed() {
    return useCaseFailed;
  }

  public String getError() {
    return error;
  }
}
