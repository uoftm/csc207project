package entities.rooms;

import entities.auth.DisplayUser;
import java.util.List;

public class Room {
  private final String uid;
  private final String name;
  private List<DisplayUser> users;
  private List<Message> messages;

  public Room(String uid, String name, List<DisplayUser> users, List<Message> messages) {
    this.uid = uid;
    this.name = name;
    this.users = users;
    this.messages = messages;
  }

  /**
   * @return the unique identifier of the Room (can be any arbitrary string).
   */
  public String getUid() {
    return uid;
  }

  /**
   * @return the user visible display name of the Room.
   */
  public String getName() {
    return name;
  }

  /**
   * @return the list of users with permissions to view messages in the Room.
   */
  public List<DisplayUser> getUsers() {
    return users;
  }

  /**
   * @return the list of messages in the Room.
   */
  public List<Message> getMessages() {
    return messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }
}
