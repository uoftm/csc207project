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
  /** Prevent instantiation. */
  private SettingsUseCaseFactory() {}

  /**
   * Creates the Settings page and the use case for it
   *
   * @return
   */
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

  /**
   * Create the settings controller, for internal use
   *
   * @return The instance of the SettingsController
   */
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
