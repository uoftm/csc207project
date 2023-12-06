package app;

import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsPresenter;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.io.IOException;
import javax.swing.*;

import use_case.login.LoginUserDataAccessInterface;
import use_case.settings.*;
import view.LoginView;
import view.SettingsView;

public class SettingsUseCaseFactory {

  private SettingsUseCaseFactory() {}

  public static SettingsView create(
      SettingsViewModel settingsViewModel,
      UserSettingsDataAccessInterface userSettingsDataAccessObject,
      RoomsSettingsDataAccessInterface roomsSettingsDataAccessInterface,
      LoginUserDataAccessInterface userDao,
      SwitchViewController switchViewController) {

    try {
      SettingsController settingsController =
          createSettingsController(
              settingsViewModel, userSettingsDataAccessObject, roomsSettingsDataAccessInterface, userDao);

      return new SettingsView(settingsViewModel, settingsController, switchViewController);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Not available.");
      return null;
    }
  }

  private static SettingsController createSettingsController(
          SettingsViewModel settingsViewModel,
          UserSettingsDataAccessInterface userSettingsDataAccessObject,
          RoomsSettingsDataAccessInterface roomsSettingsDataAccessInterface,
          LoginUserDataAccessInterface userDao)
      throws IOException {

    SettingsOutputBoundary settingsOutputBoundary = new SettingsPresenter(settingsViewModel);
    SettingsInputBoundary settingsInteractor =
        new SettingsInteractor(
            userSettingsDataAccessObject, roomsSettingsDataAccessInterface, userDao, settingsOutputBoundary);

    return new SettingsController(settingsInteractor);
  }
}
