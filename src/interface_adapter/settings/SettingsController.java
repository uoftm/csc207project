package interface_adapter.settings;

import use_case.settings.SettingsInputBoundary;
import use_case.settings.SettingsInputData;
import entities.auth.User;

public class SettingsController {

  final SettingsInputBoundary settingsUseCaseInteractor;

  public SettingsController(SettingsInputBoundary settingsUseCaseInteractor) {
    this.settingsUseCaseInteractor = settingsUseCaseInteractor;
  }

  public void executeChangeUsername(String username, User user) {
    SettingsInputData settingsInputData = new SettingsInputData(username, user);
    settingsUseCaseInteractor.executeChangeUsername(settingsInputData);
  }
}
