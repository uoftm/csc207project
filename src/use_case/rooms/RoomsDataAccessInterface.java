package use_case.rooms;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;

import java.util.List;

public interface RoomsDataAccessInterface {
  Response<List<Message>> loadMessages(Room room, User user);
  Response<String> sendMessage(Room room, User user, String message);
  boolean validateRoomAccess(Room room, User user);
  Response<String> addUserToRoom(Room room, User user, String email);
  Response<Room> createRoom(User user, String createRoom);
}
