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

  @Override
  public void prepareSuccessView(RoomsOutputData response) {
    RoomsState roomsState = roomsViewModel.getState();
    roomsState.setSuccess(response.getSuccess());
    roomsViewModel.firePropertyChanged();
  }

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

  @Override
  public void prepareCreateRoomSuccessView(RoomsOutputData response) {
    RoomsState roomsState = roomsViewModel.getState();
    List<Room> rooms = roomsState.getAvailableRooms();
    rooms.add(response.getRoom());
    roomsState.setAvailableRooms(rooms);
    roomsState.setRoomUid(response.getRoom().getUid());
    roomsViewModel.firePropertyChanged();
  }

  @Override
  public void prepareFailView(RoomsOutputData response) {
    RoomsState roomsState = roomsViewModel.getState();
    roomsState.setError(response.getError());
    roomsViewModel.firePropertyChanged();
  }
}
