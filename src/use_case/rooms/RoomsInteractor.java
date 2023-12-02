package use_case.rooms;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;

import java.util.List;

public class RoomsInteractor implements RoomsInputBoundary {
  final RoomsOutputBoundary roomsPresenter;
  final RoomsDataAccessInterface roomsDataAccessObject;

  public RoomsInteractor(
      RoomsDataAccessInterface roomsDataAccessObject, RoomsOutputBoundary roomsOutputBoundary) {
    this.roomsPresenter = roomsOutputBoundary;
    this.roomsDataAccessObject = roomsDataAccessObject;
  }

  @Override
  public void loadMessages(RoomsInputData roomsInputData) {
    Room room = roomsInputData.getRoom();
    User user = roomsInputData.getUser();

    boolean valid = roomsDataAccessObject.validateRoomAccess(room, user);

    if (valid) {
      Response<List<Message>> response = roomsDataAccessObject.loadMessages(room, user);
      if (response.isError()) {
        RoomsOutputData roomsOutputData = new RoomsOutputData(null, null, null, response.getError(), null);
        roomsPresenter.prepareFailView(roomsOutputData);
      } else {
        RoomsOutputData roomsOutputData = new RoomsOutputData(null, null, response.getVal(), null, null);
        roomsPresenter.prepareLoadMessagesSuccessView(roomsOutputData);
      }
    } else {
      String error = "Access to Room " + room.getName() + " denied";
      RoomsOutputData roomsOutputData = new RoomsOutputData(null, null, null, error, null);
      roomsPresenter.prepareFailView(roomsOutputData);
    }
  }

  @Override
  public void sendMessage(RoomsInputData roomsInputData) {
    Room room = roomsInputData.getRoom();
    User user = roomsInputData.getUser();
    String message = roomsInputData.getMessage();

    boolean valid = roomsDataAccessObject.validateRoomAccess(room, user);

    if (valid) {
      Response<String> response = roomsDataAccessObject.sendMessage(room, user, message);
      if (response.isError()) {
        RoomsOutputData roomsOutputData = new RoomsOutputData(null, null, null, response.getError(), null);
        roomsPresenter.prepareFailView(roomsOutputData);
      }
    } else {
      String error = "Access to Room " + room.getName() + " denied";
      RoomsOutputData roomsOutputData = new RoomsOutputData(null, null, null, error, null);
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
        RoomsOutputData roomsOutputData = new RoomsOutputData(null, null, null, response.getError(), null);
        roomsPresenter.prepareFailView(roomsOutputData);
      } else {
        RoomsOutputData roomsOutputData = new RoomsOutputData(null, null, null, null, response.getVal());
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
    String createRoom = roomsInputData.getCreateRoom();

    Response<Room> response = roomsDataAccessObject.createRoom(user, createRoom);
    if (response.isError()) {
      RoomsOutputData roomsOutputData = new RoomsOutputData(null, null, null, response.getError(), null);
      roomsPresenter.prepareFailView(roomsOutputData);
    } else {
      RoomsOutputData roomsOutputData = new RoomsOutputData(response.getVal(), null, null, null, null);
      roomsPresenter.prepareCreateRoomSuccessView(roomsOutputData);
    }
  }
}
