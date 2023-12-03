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
}
