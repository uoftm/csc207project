package interface_adapter.rooms;

import entities.rooms.Room;
import use_case.rooms.LoadRoomsOutputBoundary;
import use_case.rooms.LoadRoomsOutputData;

public class LoadRoomsPresenter implements LoadRoomsOutputBoundary {
  private final RoomsViewModel roomsViewModel;

  public LoadRoomsPresenter(RoomsViewModel roomsViewModel) {
    this.roomsViewModel = roomsViewModel;
  }

  @Override
  public void prepareSuccessView(LoadRoomsOutputData output) {
    RoomsState roomsState = roomsViewModel.getState();
    roomsState.setAvailableRooms(output.getRooms());

    // Update display messages for currently selected room
    if (roomsState.roomIsSelected()) {
      Room room = roomsState.getRoomByUid();
      roomsState.setDisplayMessages(room.getMessages());
    }

    roomsViewModel.firePropertyChanged();
  }

  @Override
  public void prepareFailView(LoadRoomsOutputData response) {
    RoomsState roomsState = roomsViewModel.getState();
    roomsState.setError(response.getError());
    roomsViewModel.firePropertyChanged();
  }
}
