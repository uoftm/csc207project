package interface_adapter.room_settings;

import entities.rooms.Room;
import interface_adapter.ViewManagerModel;
import interface_adapter.rooms.RoomsState;
import interface_adapter.rooms.RoomsViewModel;
import use_case.room_settings.RoomSettingsOutputBoundary;
import view.LoggedInView;

public class RoomSettingsPresenter implements RoomSettingsOutputBoundary {
  private final RoomsViewModel roomsViewModel;
  private final RoomSettingsViewModel roomSettingsViewModel;
  private final ViewManagerModel viewManagerModel;

  public RoomSettingsPresenter(
      RoomsViewModel roomsViewModel,
      RoomSettingsViewModel roomSettingsViewModel,
      ViewManagerModel viewManagerModel) {
    this.roomsViewModel = roomsViewModel;
    this.roomSettingsViewModel = roomSettingsViewModel;
    this.viewManagerModel = viewManagerModel;
  }

  public void prepareDeleteRoomSuccessView(Room room) {
    RoomsState state = roomsViewModel.getState();
    var availableRooms = state.getAvailableRooms();
    availableRooms.remove(room);
    state.setRoomUid(null); // Clear the displayed room (display blank)
    roomsViewModel.setState(state);
    roomsViewModel.firePropertyChanged();

    viewManagerModel.setActiveView(LoggedInView.viewName);
  }

  public void prepareChangeRoomFailView(String message) {
    roomSettingsViewModel.setError(message);
  }

  public void prepareChangeRoomNameSuccessView(Room activeRoom, String roomName) {
    RoomsState state = roomsViewModel.getState();
    var availableRooms = state.getAvailableRooms();
    for (Room room : availableRooms) {
      if (room.getUid().equals(activeRoom.getUid())) {
        room.setName(roomName);
        break;
      }
    }
    roomsViewModel.setState(state);
    roomsViewModel.firePropertyChanged();

    viewManagerModel.setActiveView(LoggedInView.viewName);
  }

  public void prepareDeleteRoomFailView(String message) {
    roomSettingsViewModel.setError(message);
  }
}
