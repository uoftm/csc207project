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
  final LoggedInDataAccessInterface inMemoryDAO;

  public RoomsInteractor(
      RoomsDataAccessInterface roomsDataAccessObject,
      MessageDataAccessInterface messageDataAccessInterface,
      LoginUserDataAccessInterface userDao,
      LoggedInDataAccessInterface inMemoryDAO,
      RoomsOutputBoundary roomsOutputBoundary) {
    this.roomsPresenter = roomsOutputBoundary;
    this.roomsDataAccessObject = roomsDataAccessObject;
    this.messageDataAccessInterface = messageDataAccessInterface;
    this.userDao = userDao;
    this.inMemoryDAO = inMemoryDAO;
  }

  @Override
  public void sendMessage(RoomsInputData roomsInputData) {
    try {
      Room room = roomsInputData.getRoom();
      User user = inMemoryDAO.getUser();

      try {
        messageDataAccessInterface.sendMessage(room, userDao, user, roomsInputData.getMessage());
        RoomsOutputData roomsOutputData =
            new RoomsOutputData(room,null, roomsInputData.getMessage());
        roomsPresenter.prepareSendMessageSuccessView(roomsOutputData);
      } catch (RuntimeException e) {
        RoomsOutputData roomsOutputData =
            new RoomsOutputData(null, e.getMessage(), null);
        roomsPresenter.prepareFailView(roomsOutputData);
      }

      RoomsOutputData roomsOutputData =
          new RoomsOutputData(room,null, "Success");
      roomsPresenter.prepareSuccessView(roomsOutputData);
    } catch (RuntimeException e) {
      RoomsOutputData roomsOutputData =
          new RoomsOutputData(null, e.getMessage(), null);
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }

  @Override
  public void addUserToRoom(RoomsInputData roomsInputData) {
    Room room = roomsInputData.getRoom();
    User user = inMemoryDAO.getUser();
    String email = roomsInputData.getEmail();

    DisplayUser displayUserFromEmail = userDao.getDisplayUser(email);

    try {
      String idToken = inMemoryDAO.getIdToken();
      roomsDataAccessObject.addUserToRoom(idToken, user, displayUserFromEmail, room);
      RoomsOutputData roomsOutputData =
          new RoomsOutputData(null, null, "Successfully added " + email);
      roomsPresenter.prepareSuccessView(roomsOutputData);
    } catch (RuntimeException e) {
      RoomsOutputData roomsOutputData =
          new RoomsOutputData(
              null, "Unable to add " + email + " to room:" + e.getMessage(), null);
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }

  @Override
  public void createRoom(RoomsInputData roomsInputData) {
    User user = inMemoryDAO.getUser();
    String roomToCreateName = roomsInputData.getRoomToCreateName();

    try {
      String idToken = inMemoryDAO.getIdToken();
      Room newRoom = roomsDataAccessObject.addRoom(idToken, user, roomToCreateName);
      RoomsOutputData roomsOutputData = new RoomsOutputData(newRoom, null, null);
      roomsPresenter.prepareCreateRoomSuccessView(roomsOutputData);
    } catch (RuntimeException e) {
      RoomsOutputData roomsOutputData = new RoomsOutputData(null, e.getMessage(), null);
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }
}
