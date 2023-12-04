package use_case.settings;

import entities.auth.AbstractUser;

public class SettingsInteractor implements SettingsInputBoundary {
  // TODO: Add this after other code is merged
  // final SettingsUserDataAccessInterface settingsUserDataAccessInterface;
  final SettingsOutputBoundary settingsPresenter;

  public SettingsInteractor(
      // SettingsUserDataAccessInterface settingsDataAccessInterface,
      SettingsOutputBoundary settingsOutputBoundary) {
    // this.settingsDataAccessObject = settingsDataAccessInterface;
    this.settingsPresenter = settingsOutputBoundary;
  }

  @Override
  public void execute(SettingsInputData userInputData) {
    AbstractUser user = userInputData.getUser();

    // TODO: Write interaction after other code is merged
    SettingsOutputData settingsOutputData = new SettingsOutputData(user, true);
    settingsPresenter.prepareSuccessView(settingsOutputData);
  }

  @Override
  public void executeChangeUsername(SettingsInputData settingsInputData) {

    
  }
}
