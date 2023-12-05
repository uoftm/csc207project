package interface_adapter.room_settings;

import entities.rooms.Room;
import interface_adapter.ViewManagerModel;
import interface_adapter.rooms.RoomsState;
import interface_adapter.rooms.RoomsViewModel;
import use_case.room_settings.RoomSettingsOutputBoundary;
import view.LoggedInView;

public class RoomSettingsPresenter implements RoomSettingsOutputBoundary {
  private final RoomsViewModel roomsViewModel;
  private final ViewManagerModel viewManagerModel;

  public RoomSettingsPresenter(RoomsViewModel roomsViewModel, ViewManagerModel viewManagerModel) {
    this.roomsViewModel = roomsViewModel;
    this.viewManagerModel = viewManagerModel;
  }

  public void deletedRoom(Room room) {
    RoomsState state = roomsViewModel.getState();
    var availableRooms = state.getAvailableRooms();
    availableRooms.remove(room);
    if (!availableRooms.isEmpty()) {
      state.setRoomUid(null);
    } else {
      state.setRoomUid(availableRooms.get(0).getUid());
    }
    roomsViewModel.setState(state);
    roomsViewModel.firePropertyChanged();

    viewManagerModel.setActiveView(LoggedInView.viewName);
  }

  public void savedRoomName(Room activeRoom, String roomName) {
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
}
