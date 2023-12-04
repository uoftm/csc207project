package interface_adapter.rooms;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import java.util.ArrayList;
import java.util.List;

public class RoomsState {
  private String roomUid;
  private User user;
  private List<Room> availableRooms = new ArrayList<>();
  private List<Message> displayMessages = new ArrayList<>();
  private String sendMessage;
  private String userToAddEmail;
  private String roomToCreateName;
  private String error;
  private String success;

  public RoomsState() {}

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getUserUid() {
    return user.getUid();
  }

  public String getRoomUid() {
    return roomUid;
  }

  public void setRoomUid(String roomUid) {
    this.roomUid = roomUid;
  }

  public List<Room> getAvailableRooms() {
    return availableRooms;
  }

  public void setAvailableRooms(List<Room> availableRooms) {
    this.availableRooms = availableRooms;
  }

  public List<Message> getDisplayMessages() {
    return displayMessages;
  }

  public void setDisplayMessages(List<Message> displayMessages) {
    this.displayMessages = displayMessages;
  }

  public String getSendMessage() {
    return sendMessage;
  }

  public void setSendMessage(String sendMessage) {
    this.sendMessage = sendMessage;
  }

  public String getUserToAddEmail() {
    return userToAddEmail;
  }

  public void setUserToAddEmail(String userToAddEmail) {
    this.userToAddEmail = userToAddEmail;
  }

  public String getRoomToCreateName() {
    return roomToCreateName;
  }

  public void setRoomToCreateName(String roomToCreateName) {
    this.roomToCreateName = roomToCreateName;
  }

  public String getError() {
    return this.error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getSuccess() {
    return this.success;
  }

  public void setSuccess(String success) {
    this.success = success;
  }
}
