package use_case.rooms;

import entities.rooms.Message;
import java.util.List;

public interface RoomsDataAccessInterface {
  List<Message> getAllMessages(); // Example usage

  boolean validateRoomAccess(String roomUid, String userUid); // Example usage
}
