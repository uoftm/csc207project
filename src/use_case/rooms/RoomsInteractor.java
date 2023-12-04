package use_case.rooms;

import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Room;
import java.util.ArrayList;
import use_case.login.LoginUserDataAccessInterface;

public class RoomsInteractor implements RoomsInputBoundary {
  // TODO: Split up these distinct calls into their own interactors
  final RoomsOutputBoundary roomsPresenter;
  final RoomsDataAccessInterface roomsDataAccessObject;
  // TODO: Make a different interface to this dao for the rooms module
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

      RoomsOutputData roomsOutputData =
          new RoomsOutputData(room, user, new ArrayList<>(), null, "Success");
      roomsPresenter.prepareSuccessView(roomsOutputData);
    } catch (RuntimeException e) {
      RoomsOutputData roomsOutputData =
          new RoomsOutputData(null, null, new ArrayList<>(), e.getMessage(), null);
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

      RoomsOutputData roomsOutputData =
          new RoomsOutputData(room, user, new ArrayList<>(), null, "Success");
      roomsPresenter.prepareSuccessView(roomsOutputData);
    } catch (RuntimeException e) {
      RoomsOutputData roomsOutputData =
          new RoomsOutputData(null, null, new ArrayList<>(), e.getMessage(), null);
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }

  @Override
  public void addUserToRoom(RoomsInputData roomsInputData) {
    Room room = roomsInputData.getRoom();
    User user = roomsInputData.getUser();
    String email = roomsInputData.getEmail();

    DisplayUser displayUserFromEmail = userDao.getDisplayUser(email);

    try {
      roomsDataAccessObject.addUserToRoom(user, displayUserFromEmail, userDao, room);
      RoomsOutputData roomsOutputData = new RoomsOutputData(null, null, null, null, "Success");
      roomsPresenter.prepareSuccessView(roomsOutputData);
    } catch (RuntimeException e) {
      RoomsOutputData roomsOutputData = new RoomsOutputData(null, null, null, e.getMessage(), null);
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }

  @Override
  public void createRoom(RoomsInputData roomsInputData) {
    User user = roomsInputData.getUser();
    String roomToCreateName = roomsInputData.getRoomToCreateName();

    try {
      Room newRoom = roomsDataAccessObject.addRoom(user, userDao, roomToCreateName);
      RoomsOutputData roomsOutputData = new RoomsOutputData(newRoom, null, null, null, null);
      roomsPresenter.prepareCreateRoomSuccessView(roomsOutputData);
    } catch (RuntimeException e) {
      RoomsOutputData roomsOutputData = new RoomsOutputData(null, null, null, e.getMessage(), null);
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }
}
