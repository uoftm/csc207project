package use_case.rooms;

import entities.auth.User;
import entities.rooms.Room;

/**
 * Represents the input data for rooms-related operations.
 *
 * <p>This is intended to be used as a sum-type, where only one of the message, userToAddEmail,
 * roomToCreateName fields is non-null.
 */
public class RoomsInputData {
  Room room;
  User user;
  String message;
  String userToAddEmail;
  String roomToCreateName;

  public RoomsInputData(
      Room room, User user, String message, String userToAddEmail, String roomToCreateName) {
    this.room = room;
    this.user = user;
    this.message = message;
    this.userToAddEmail = userToAddEmail;
    this.roomToCreateName = roomToCreateName;
  }

  /**
   * @return the Room object associated with this instance.
   */
  Room getRoom() {
    return room;
  }

  /**
   * @return the logged in User
   */
  User getUser() {
    return user;
  }

  /**
   * @return The message to send.
   */
  String getMessage() {
    return message;
  }

  /**
   * @return The email of the user to add to the room.
   */
  String getEmail() {
    return userToAddEmail;
  }

  /**
   * @return The name of the room to create.
   */
  String getRoomToCreateName() {
    return roomToCreateName;
  }
}
