package entities.rooms;

import entities.auth.DisplayUser;
import java.util.List;

public class Room {
  private final String uid;
  private String name;
  private final List<DisplayUser> users;
  private List<Message> messages;

  public Room(String uid, String name, List<DisplayUser> users, List<Message> messages) {
    this.uid = uid;
    this.name = name;
    this.users = users;
    this.messages = messages;
  }

  public String getUid() {
    return uid;
  }

  public String getName() {
    return name;
  }

  public List<DisplayUser> getUsers() {
    return users;
  }

  public List<Message> getMessages() {
    return messages;
  }

  public void setMessages(List<Message> messages) {
    this.messages = messages;
  }

  public void setName(String name) {
    this.name = name;
  }
}
