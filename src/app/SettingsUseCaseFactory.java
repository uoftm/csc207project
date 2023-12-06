package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsPresenter;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.io.IOException;
import javax.swing.*;
import use_case.login.LoginUserDataAccessInterface;
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
      LoginUserDataAccessInterface userDao,
      LoggedInDataAccessInterface inMemoryDAO,
      SwitchViewController switchViewController) {

    try {
      SettingsController settingsController =
          createSettingsController(
              settingsViewModel,
              loggedInViewModel,
              userSettingsDataAccessObject,
              roomsSettingsDataAccessObject,
              userDao,
                  inMemoryDAO);

      return new SettingsView(settingsViewModel, settingsController, switchViewController);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Not available.");
      return null;
    }
  }

  private static SettingsController createSettingsController(
      SettingsViewModel settingsViewModel,
      LoggedInViewModel loggedInViewModel,
      UserSettingsDataAccessInterface userSettingsDataAccessObject,
      RoomsSettingsDataAccessInterface roomsSettingsDataAccessInterface,
      LoginUserDataAccessInterface userDao,
      LoggedInDataAccessInterface inMemoryDAO)
      throws IOException {

    SettingsOutputBoundary settingsOutputBoundary = new SettingsPresenter(settingsViewModel, loggedInViewModel);
    SettingsInputBoundary settingsInteractor =
        new SettingsInteractor(
            userSettingsDataAccessObject,
            roomsSettingsDataAccessInterface,
            userDao,
            inMemoryDAO,
            settingsOutputBoundary);

    return new SettingsController(settingsInteractor);
  }
}
