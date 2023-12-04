package use_case.rooms;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import use_case.login.LoginUserDataAccessInterface;

public interface MessageDataAccessInterface {
  void sendMessage(Room room, LoginUserDataAccessInterface userDAO, User user, String messageBody);
}
