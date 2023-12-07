package use_case.rooms;

import entities.rooms.Room;

public interface MessageDataAccessInterface {
  void sendMessage(Room room, LoggedInDataAccessInterface userDAO, String messageBody);
}
