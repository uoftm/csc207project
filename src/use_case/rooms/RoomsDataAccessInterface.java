package use_case.rooms;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import java.util.List;

public interface RoomsDataAccessInterface {
  RoomsResponse<List<Message>> loadMessages(Room room, User user);

  RoomsResponse<String> sendMessage(Room room, User user, String message);

  boolean validateRoomAccess(Room room, User user);

  RoomsResponse<String> addUserToRoom(Room room, User user, String email);

  RoomsResponse<Room> createRoom(User user, String roomToCreateName);
}
