package use_case.rooms;

import entities.Message;
import entities.user_entities.DisplayUser;
import java.util.List;

public class RoomsOutputData {
  String uid;
  String name;
  List<DisplayUser> users;
  List<Message> messages;
  private final boolean useCaseFailed;
  private final String error; // null iff useCaseFailed is false

  public RoomsOutputData(String uid, boolean useCaseFailed, String error) {
    this.uid = uid;
    this.useCaseFailed = useCaseFailed;
    this.error = error;
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

  public boolean getUseCaseFailed() {
    return useCaseFailed;
  }

  public String getError() {
    return error;
  }
}
