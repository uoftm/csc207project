package interface_adapter.rooms;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import java.util.List;

public class RoomsState {
  private String roomUid;
  private User user;
  private List<Room> availableRooms;
  private List<Message> displayMessages;
  private String sendMessage;
  private String email;  // Other user email
  private String createRoom;  // Room to create
  private String error;  // Nullable field
  private String success;

  public RoomsState(RoomsState copy) {
    roomUid = copy.roomUid;
    user = copy.user;
    availableRooms = copy.availableRooms;
    displayMessages = copy.displayMessages;
    sendMessage = copy.sendMessage;
    email = copy.email;
    error = copy.error;
    success = copy.success;
  }

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

  public String getEmail() { return email; }

  public void setEmail(String email) { this.email = email; }

  public String getCreateRoom() { return createRoom; }

  public void setCreateRoom(String createRoom) { this.createRoom = createRoom; }

  public String getError() { return this.error; }

  public void setError(String error) { this.error = error; }

  public String getSuccess() { return this.success; }

  public void setSuccess(String success) { this.success = success; }
}
