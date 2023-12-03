package use_case.rooms;

import entities.auth.User;
import entities.rooms.Room;
import use_case.login.LoginUserDataAccessInterface;

public class RoomsInteractor implements RoomsInputBoundary {
  // TODO: Split up these distinct calls into their own interactors
  final RoomsOutputBoundary roomsPresenter;
  final RoomsDataAccessInterface roomsDataAccessObject;
  final LoginUserDataAccessInterface userDao;

  public RoomsInteractor(
      RoomsDataAccessInterface roomsDataAccessObject,
      LoginUserDataAccessInterface userDao,
      RoomsOutputBoundary roomsOutputBoundary) {
    this.roomsPresenter = roomsOutputBoundary;
    this.roomsDataAccessObject = roomsDataAccessObject;
    this.userDao = userDao;
  }

  @Override
  public void loadMessages(RoomsInputData roomsInputData) {
    try {
      Room room = roomsInputData.getRoom();
      User user = roomsInputData.getUser();

      // TODO: Get messages from message DAO

      RoomsOutputData roomsOutputData = new RoomsOutputData(room, user, false, null);
      roomsPresenter.prepareSuccessView(roomsOutputData);
    } catch (RuntimeException e) {
      RoomsOutputData roomsOutputData = new RoomsOutputData(null, null, true, e.getMessage());
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }

  @Override
  public void sendMessage(RoomsInputData roomsInputData) {
    try {
      Room room = roomsInputData.getRoom();
      User user = roomsInputData.getUser();

      String message = roomsInputData.getMessage();

      // TODO: Send message via message DAO

      RoomsOutputData roomsOutputData = new RoomsOutputData(room, user, false, null);
      roomsPresenter.prepareSuccessView(roomsOutputData);
    } catch (RuntimeException e) {
      RoomsOutputData roomsOutputData = new RoomsOutputData(null, null, true, e.getMessage());
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }

  @Override
  public void addUserToRoom(RoomsInputData roomsInputData) {
    Room room = roomsInputData.getRoom();
    User user = roomsInputData.getUser();
    String email = roomsInputData.getEmail();

    boolean valid = roomsDataAccessObject.validateRoomAccess(room, user);

    if (valid) {
      Response<String> response = roomsDataAccessObject.addUserToRoom(room, user, email);
      if (response.isError()) {
        RoomsOutputData roomsOutputData =
            new RoomsOutputData(null, null, null, response.getError(), null);
        roomsPresenter.prepareFailView(roomsOutputData);
      } else {
        RoomsOutputData roomsOutputData =
            new RoomsOutputData(null, null, null, null, response.getVal());
        roomsPresenter.prepareSuccessView(roomsOutputData);
      }
    } else {
      String error = "Access to Room " + room.getName() + " denied";
      RoomsOutputData roomsOutputData = new RoomsOutputData(null, null, null, error, null);
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }

  @Override
  public void createRoom(RoomsInputData roomsInputData) {
    User user = roomsInputData.getUser();
    String roomToCreateName = roomsInputData.getRoomToCreateName();

    Response<Room> response = roomsDataAccessObject.createRoom(user, roomToCreateName);
    if (response.isError()) {
      RoomsOutputData roomsOutputData =
          new RoomsOutputData(null, null, null, response.getError(), null);
      roomsPresenter.prepareFailView(roomsOutputData);
    } else {
      RoomsOutputData roomsOutputData =
          new RoomsOutputData(response.getVal(), null, null, null, null);
      roomsPresenter.prepareCreateRoomSuccessView(roomsOutputData);
    }
  }
}
