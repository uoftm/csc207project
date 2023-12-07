package app;

import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsPresenter;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import javax.swing.*;
import use_case.login.LoginUserDataAccessInterface;
import use_case.settings.*;
import view.SettingsView;

public class SettingsUseCaseFactory {

  private SettingsUseCaseFactory() {}

  public static SettingsView create(
      SettingsViewModel settingsViewModel,
      LoggedInViewModel loggedInViewModel,
      UserSettingsDataAccessInterface userSettingsDataAccessObject,
      RoomsSettingsDataAccessInterface roomsSettingsDataAccessObject,
      LoginUserDataAccessInterface userDao,
      SwitchViewController switchViewController) {

    SettingsController settingsController =
        createSettingsController(
            settingsViewModel,
            loggedInViewModel,
            userSettingsDataAccessObject,
            roomsSettingsDataAccessObject,
            userDao);

    return new SettingsView(settingsViewModel, settingsController, switchViewController);
  }

  private static SettingsController createSettingsController(
      SettingsViewModel settingsViewModel,
      LoggedInViewModel loggedInViewModel,
      UserSettingsDataAccessInterface userSettingsDataAccessObject,
      RoomsSettingsDataAccessInterface roomsSettingsDataAccessInterface,
      LoginUserDataAccessInterface userDao) {

    SettingsOutputBoundary settingsOutputBoundary =
        new SettingsPresenter(settingsViewModel, loggedInViewModel);
    SettingsInputBoundary settingsInteractor =
        new SettingsInteractor(
            userSettingsDataAccessObject,
            roomsSettingsDataAccessInterface,
            userDao,
            settingsOutputBoundary);

    return new SettingsController(settingsInteractor);
  }
}
