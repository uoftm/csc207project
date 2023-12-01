package interface_adapter.settings;

import entities.auth.AbstractUser;
import use_case.settings.SettingsInputBoundary;
import use_case.settings.SettingsInputData;

public class SettingsController {

  final SettingsInputBoundary settingsUseCaseInteractor;

  public SettingsController(SettingsInputBoundary settingsUseCaseInteractor) {
    this.settingsUseCaseInteractor = settingsUseCaseInteractor;
  }

  public void execute(AbstractUser user) {
    SettingsInputData settingsInputData = new SettingsInputData(user);
    settingsUseCaseInteractor.execute(settingsInputData);
  }
}
