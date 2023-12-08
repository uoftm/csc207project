package interface_adapter.settings;

import use_case.settings.SettingsInputBoundary;
import use_case.settings.SettingsInputData;

public class SettingsController {
  final SettingsInputBoundary settingsUseCaseInteractor;

  /**
   * SettingsController Class
   *
   * <p>This class is responsible for controlling the settings view and interacting with the use
   * case interactor. It receives the necessary input data, performs operations based on the input
   * data, and delegates the execution to the use case interactor. It holds a reference to the
   * SettingsInputBoundary interface.
   */
  public SettingsController(SettingsInputBoundary settingsUseCaseInteractor) {
    this.settingsUseCaseInteractor = settingsUseCaseInteractor;
  }

  /**
   * Executes the change username operation.
   *
   * @param username The new username to be set
   */
  public void executeChangeUsername(String username) {
    SettingsInputData settingsInputData = new SettingsInputData(username);
    settingsUseCaseInteractor.executeChangeUsername(settingsInputData);
  }
}
