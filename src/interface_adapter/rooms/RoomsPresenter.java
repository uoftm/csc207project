package interface_adapter.rooms;

import entities.rooms.Message;
import entities.rooms.Room;
import java.util.List;
import javax.swing.*;
import use_case.rooms.RoomsOutputBoundary;
import use_case.rooms.RoomsOutputData;

public class RoomsPresenter implements RoomsOutputBoundary {
  private final RoomsViewModel roomsViewModel;

  public RoomsPresenter(RoomsViewModel roomsViewModel) {
    this.roomsViewModel = roomsViewModel;
  }

  /**
   * @param response The response data containing the information about the currently active room.
   */
  @Override
  public void prepareSuccessView(RoomsOutputData response) {
    RoomsState roomsState = roomsViewModel.getState();
    roomsState.setSuccess(response.getSuccess());
    roomsViewModel.firePropertyChanged();
  }

  /**
   * Update the view model with the messages for the currently active room.
   *
   * @param response The RoomsOutputData containing the relevant data after a successful fetch.
   */
  @Override
  public void prepareLoadMessagesSuccessView(RoomsOutputData response) {
    RoomsState roomsState = roomsViewModel.getState();
    List<Message> messages = response.getMessages();
    roomsState.setDisplayMessages(messages);

    String roomUid = roomsState.getRoomUid();
    for (var room : roomsState.getAvailableRooms()) {
      if (room.getUid().equals(roomUid)) {
        room.setMessages(messages);
      }
    }

    roomsViewModel.firePropertyChanged();
  }

  /** Add a new room to the view model. */
  @Override
  public void prepareCreateRoomSuccessView(RoomsOutputData response) {
    RoomsState roomsState = roomsViewModel.getState();
    List<Room> rooms = roomsState.getAvailableRooms();
    rooms.add(response.getRoom());
    roomsState.setAvailableRooms(rooms);
    roomsState.setRoomUid(response.getRoom().getUid());
    roomsViewModel.firePropertyChanged();
  }

  /** Show a popup of a failed response from the server. */
  @Override
  public void prepareFailView(RoomsOutputData response) {
    RoomsState roomsState = roomsViewModel.getState();
    roomsState.setError(response.getError());
    roomsViewModel.firePropertyChanged();
  }
}
