package interface_adapter.rooms;

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
    roomsViewModel.firePropertyChanged();
  }

  @Override
  public void prepareFailView(LoadRoomsOutputData response) {
    // On error, switch back to the same view for now
    System.out.println(response.getError());
  }
}
