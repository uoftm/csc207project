package use_case.rooms;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import java.util.List;

public class RoomsOutputData {
  private final User user;
  private final Room room;
  private final List<Message> messages;
  private final String error;
  private final String success;

  public RoomsOutputData(
      Room room, User user, List<Message> messages, String error, String success) {
    this.room = room;
    this.user = user;
    this.messages = messages;
    this.error = error;
    this.success = success;
  }

  public Room getRoom() {
    return room;
  }

  public User getUser() {
    return user;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public String getError() {
    return error;
  }

  public String getSuccess() {
    return success;
  }
}
