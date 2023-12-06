package interface_adapter.room_settings;

import entities.rooms.Room;
import interface_adapter.ViewManagerModel;
import interface_adapter.rooms.RoomsState;
import interface_adapter.rooms.RoomsViewModel;
import use_case.room_settings.RoomSettingsOutputBoundary;
import use_case.room_settings.RoomSettingsOutputData;
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

  public void prepareDeleteRoomSuccessView(RoomSettingsOutputData outputData) {
    RoomsState state = roomsViewModel.getState();
    var availableRooms = state.getAvailableRooms();
    availableRooms.remove(outputData.getRoom());
    state.setRoomUid(null); // Clear the displayed room (display blank)
    roomsViewModel.setState(state);
    roomsViewModel.firePropertyChanged();

    viewManagerModel.setActiveView(LoggedInView.viewName);
  }

  public void prepareChangeRoomFailView(RoomSettingsOutputData outputData) {
    roomSettingsViewModel.setError(outputData.getMessage());
  }

  public void prepareChangeRoomNameSuccessView(RoomSettingsOutputData outputData) {
    Room activeRoom = outputData.getRoom();
    String roomName = activeRoom.getName();
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

  public void prepareDeleteRoomFailView(RoomSettingsOutputData outputData) {
    roomSettingsViewModel.setError(outputData.getMessage());
  }
}
