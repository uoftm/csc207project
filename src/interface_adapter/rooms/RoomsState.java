package interface_adapter.rooms;

import entities.Message;
import entities.Room;
import entities.user_entities.User;
import java.util.List;

public class RoomsState {
  private String roomUid;
  private User user;
  private List<Room> availableRooms;
  private List<Message> displayMessages;
  private String sendMessage;

  public RoomsState(RoomsState copy) {
    roomUid = copy.roomUid;
    user = copy.user;
    availableRooms = copy.availableRooms;
    displayMessages = copy.displayMessages;
    sendMessage = copy.sendMessage;
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
}
