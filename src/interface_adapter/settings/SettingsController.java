package interface_adapter.settings;

import entities.auth.User;
import use_case.settings.SettingsInputBoundary;
import use_case.settings.SettingsInputData;

public class SettingsController {

  final SettingsInputBoundary settingsUseCaseInteractor;

  public SettingsController(SettingsInputBoundary settingsUseCaseInteractor) {
    this.settingsUseCaseInteractor = settingsUseCaseInteractor;
  }

  public void executeChangeUsername(String username) {
    SettingsInputData settingsInputData = new SettingsInputData(username);
    settingsUseCaseInteractor.executeChangeUsername(settingsInputData);
  }
}
