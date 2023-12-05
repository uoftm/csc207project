package use_case.rooms;

import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Room;
import java.util.ArrayList;
import use_case.login.LoginUserDataAccessInterface;

public class RoomsInteractor implements RoomsInputBoundary {
  final RoomsOutputBoundary roomsPresenter;
  final RoomsDataAccessInterface roomsDataAccessObject;
  final MessageDataAccessInterface messageDataAccessInterface;
  final LoginUserDataAccessInterface userDao;

  public RoomsInteractor(
      RoomsDataAccessInterface roomsDataAccessObject,
      MessageDataAccessInterface messageDataAccessInterface,
      LoginUserDataAccessInterface userDao,
      RoomsOutputBoundary roomsOutputBoundary) {
    this.roomsPresenter = roomsOutputBoundary;
    this.roomsDataAccessObject = roomsDataAccessObject;
    this.messageDataAccessInterface = messageDataAccessInterface;
    this.userDao = userDao;
  }

  @Override
  public void sendMessage(RoomsInputData roomsInputData) {
    try {
      Room room = roomsInputData.getRoom();
      User user = roomsInputData.getUser();

      try {
        messageDataAccessInterface.sendMessage(room, userDao, user, roomsInputData.getMessage());
        RoomsOutputData roomsOutputData =
            new RoomsOutputData(room, user, null, null, roomsInputData.getMessage());
        roomsPresenter.prepareSendMessageSuccessView(roomsOutputData);
      } catch (RuntimeException e) {
        RoomsOutputData roomsOutputData =
            new RoomsOutputData(null, null, null, e.getMessage(), null);
        roomsPresenter.prepareFailView(roomsOutputData);
      }

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
