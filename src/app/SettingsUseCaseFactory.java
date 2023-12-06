package app;

import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsPresenter;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.switch_view.SwitchViewController;
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

    SettingsController settingsController =
        createSettingsController(settingsViewModel, settingsDataAccessObject);

    return new SettingsView(settingsViewModel, settingsController, switchViewController);
  }

  private static SettingsController createSettingsController(
      SettingsViewModel settingsViewModel, SettingsDataAccessInterface settingsDataAccessObject) {

    SettingsOutputBoundary settingsOutputBoundary = new SettingsPresenter(settingsViewModel);
    SettingsInputBoundary settingsInteractor = new SettingsInteractor(settingsOutputBoundary);

    return new SettingsController(settingsInteractor);
  }
}
