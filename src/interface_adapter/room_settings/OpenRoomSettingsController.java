package interface_adapter.room_settings;

import entities.auth.User;
import entities.rooms.Room;
import interface_adapter.ViewManagerModel;
import view.RoomSettingsView;

public class OpenRoomSettingsController {
  // We diverge from Clean Architecture slightly here as is recommended on piazza:
  // https://piazza.com/class/lm84a7mo56k5wy/post/1231
  // In particular, we only need to modify the view model, and not use any data access objects or
  // other entities

  RoomSettingsViewModel roomSettingsViewModel;
  ViewManagerModel viewManagerModel;

  public OpenRoomSettingsController(
      RoomSettingsViewModel roomSettingsViewModel, ViewManagerModel viewManagerModel) {
    this.roomSettingsViewModel = roomSettingsViewModel;
    this.viewManagerModel = viewManagerModel;
  }

  public void open(Room activeRoom) {
    roomSettingsViewModel.setActiveRoom(activeRoom);

    viewManagerModel.setActiveView(RoomSettingsView.viewName);
  }
}
