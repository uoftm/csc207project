package interface_adapter.rooms;

import entity.Message;
import entity.Room;
import java.util.List;

public class RoomsState {
  private String roomUid;
  private String userUid;
  private List<Room> availableRooms;
  private List<Message> displayMessages;
  private String sendMessage;

  public RoomsState(RoomsState copy) {
    roomUid = copy.roomUid;
    userUid = copy.userUid;
    availableRooms = copy.availableRooms;
    displayMessages = copy.displayMessages;
    sendMessage = copy.sendMessage;
  }

  public RoomsState() {}

  public String getUserUid() {
    return userUid;
  }

  public void setUserUid(String userUid) {
    this.userUid = userUid;
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
