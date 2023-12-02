package interface_adapter.rooms;

import entities.auth.User;
import entities.rooms.Room;
import use_case.rooms.RoomsInputBoundary;
import use_case.rooms.RoomsInputData;

public class RoomsController {
  final RoomsInputBoundary roomsUseCaseInteractor;

  public RoomsController(RoomsInputBoundary roomsUseCaseInteractor) {
    this.roomsUseCaseInteractor = roomsUseCaseInteractor;
  }

  /**
   * Loads the messages for a given room and user.
   *
   * @param room the room to load messages for
   * @param user the active logged in user
   */
  public void loadMessages(Room room, User user) {
    RoomsInputData roomsInputData = new RoomsInputData(room, user, null, null, null);
    roomsUseCaseInteractor.loadMessages(roomsInputData);
  }

  /**
   * Sends a message to a given room.
   *
   * @param room the room to send the message to
   * @param user the logged in user
   * @param message the message to send
   */
  public void sendMessage(Room room, User user, String message) {
    RoomsInputData roomsInputData = new RoomsInputData(room, user, message, null, null);
    roomsUseCaseInteractor.sendMessage(roomsInputData);
  }

  /**
   * Adds a user to a given room (so that they can see the messages).
   *
   * @param room the room to add the user to
   * @param user the logged user so that we can check that they have permissions
   * @param userToAddEmail the email of the user to add to the room
   */
  public void addUserToRoom(Room room, User user, String userToAddEmail) {
    RoomsInputData roomsInputData = new RoomsInputData(room, user, null, userToAddEmail, null);
    roomsUseCaseInteractor.addUserToRoom(roomsInputData);
  }

  /**
   * Creates a new room with a given name.
   *
   * @param user the logged in user (who will be the owner of the new room)
   * @param roomToCreateName the name of the room to create
   */
  public void createRoom(User user, String roomToCreateName) {
    RoomsInputData roomsInputData = new RoomsInputData(null, user, null, null, roomToCreateName);
    roomsUseCaseInteractor.createRoom(roomsInputData);
  }
}
