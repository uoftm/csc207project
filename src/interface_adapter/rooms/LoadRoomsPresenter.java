package interface_adapter.rooms;

import entities.rooms.Room;
import java.util.Set;
import java.util.stream.Collectors;
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
    boolean shouldUpdateView = false;

    // Compare the two lists of rooms
    Set<String> newRooms =
        output.getRooms().stream().map(Room::getName).collect(Collectors.toSet());
    Set<String> oldRooms =
        roomsState.getAvailableRooms().stream().map(Room::getName).collect(Collectors.toSet());
    if (!newRooms.equals(oldRooms)) {
      // The rooms names have changed, so update the view
      shouldUpdateView = true;
    }
    roomsState.setAvailableRooms(output.getRooms());

    // Update display messages for currently selected room
    Room room = roomsState.getRoomByUid();
    if (roomsState.roomIsSelected() && room != null) {
      Set<Long> oldMessages =
          roomsState.getDisplayMessages().stream()
              .map(message -> message.timestamp.toEpochMilli())
              .collect(Collectors.toSet());
      Set<Long> newMessages =
          room.getMessages().stream()
              .map(message -> message.timestamp.toEpochMilli())
              .collect(Collectors.toSet());
      if (!oldMessages.equals(newMessages)) {
        // The room messages have changed, so update the view
        shouldUpdateView = true;
      }
      roomsState.setDisplayMessages(room.getMessages());
    }

    if (shouldUpdateView) {
      roomsViewModel.firePropertyChanged();
    }
  }

  @Override
  public void prepareFailView(LoadRoomsOutputData response) {
    RoomsState roomsState = roomsViewModel.getState();
    if (roomsState.getError() == null || !roomsState.getError().equals(response.getError())) {
      // Error not already seen
      roomsState.setError(response.getError());
      roomsViewModel.firePropertyChanged();
    }
  }
}
