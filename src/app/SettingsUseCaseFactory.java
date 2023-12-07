package app;

import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsPresenter;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import use_case.rooms.LoggedInDataAccessInterface;
import use_case.settings.*;
import view.SettingsView;

public class SettingsUseCaseFactory {

  private SettingsUseCaseFactory() {}

  public static SettingsView create(
      SettingsViewModel settingsViewModel,
      LoggedInViewModel loggedInViewModel,
      UserSettingsDataAccessInterface userSettingsDataAccessObject,
      RoomsSettingsDataAccessInterface roomsSettingsDataAccessObject,
      LoggedInDataAccessInterface inMemoryDAO,
      SwitchViewController switchViewController) {

    SettingsController settingsController =
        createSettingsController(
            settingsViewModel,
            loggedInViewModel,
            userSettingsDataAccessObject,
            roomsSettingsDataAccessObject,
            inMemoryDAO);

    return new SettingsView(settingsViewModel, settingsController, switchViewController);
  }

  private static SettingsController createSettingsController(
      SettingsViewModel settingsViewModel,
      LoggedInViewModel loggedInViewModel,
      UserSettingsDataAccessInterface userSettingsDataAccessObject,
      RoomsSettingsDataAccessInterface roomsSettingsDataAccessInterface,
      LoggedInDataAccessInterface inMemoryDAO) {

    SettingsOutputBoundary settingsOutputBoundary =
        new SettingsPresenter(settingsViewModel, loggedInViewModel);
    SettingsInputBoundary settingsInteractor =
        new SettingsInteractor(
            userSettingsDataAccessObject,
            roomsSettingsDataAccessInterface,
            inMemoryDAO,
            settingsOutputBoundary);

    return new SettingsController(settingsInteractor);
  }
}
