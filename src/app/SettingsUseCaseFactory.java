package app;

import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsPresenter;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.io.IOException;
import javax.swing.*;
import use_case.settings.SettingsDataAccessInterface;
import use_case.settings.SettingsInputBoundary;
import use_case.settings.SettingsInteractor;
import use_case.settings.SettingsOutputBoundary;
import view.SettingsView;

public class SettingsUseCaseFactory {

  private SettingsUseCaseFactory() {}

  public static SettingsView create(
      SettingsViewModel settingsViewModel,
      SettingsDataAccessInterface settingsDataAccessObject,
      SwitchViewController switchViewController) {

    try {
      SettingsController settingsController =
          createSettingsController(settingsViewModel, settingsDataAccessObject);

      return new SettingsView(settingsViewModel, settingsController, switchViewController);
    } catch (IOException e) {
      JOptionPane.showMessageDialog(null, "Not available.");
      return null;
    }
  }

  private static SettingsController createSettingsController(
      SettingsViewModel settingsViewModel, SettingsDataAccessInterface settingsDataAccessObject)
      throws IOException {

    SettingsOutputBoundary settingsOutputBoundary = new SettingsPresenter(settingsViewModel);
    SettingsInputBoundary settingsInteractor = new SettingsInteractor(settingsOutputBoundary);

    return new SettingsController(settingsInteractor);
  }
}
