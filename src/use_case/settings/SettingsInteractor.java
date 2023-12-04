package use_case.settings;

import entities.auth.AbstractUser;
import entities.auth.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.settings.UserSettingsDataAccessInterface;



public class SettingsInteractor implements SettingsInputBoundary {
  // TODO: Add this after other code is merged
  // final SettingsUserDataAccessInterface settingsUserDataAccessInterface;
  final SettingsOutputBoundary settingsPresenter;

  final UserSettingsDataAccessInterface userSettingsDataAccessObject;
  final RoomsSettingsDataAccessInterface roomsSettingsDataAccessObject;

  public SettingsInteractor(
      UserSettingsDataAccessInterface userSettingsDataAccessInterface,
      RoomsSettingsDataAccessInterface roomsSettingsDataAccessInterface,
      SettingsOutputBoundary settingsOutputBoundary) {
    this.userSettingsDataAccessObject = userSettingsDataAccessInterface;
    this.roomsSettingsDataAccessObject = roomsSettingsDataAccessInterface;
    this.settingsPresenter = settingsOutputBoundary;
  }

  @Override
  public void execute(SettingsInputData inputData) {

    // TODO: Write interaction after other code is merged
    SettingsOutputData settingsOutputData = new SettingsOutputData(user, true);
    settingsPresenter.prepareSuccessView(settingsOutputData);
  }

  @Override
  public void executeChangeUsername(SettingsInputData settingsInputData) {

    userSettingsDataAccessObject.changeDisplayName(user, settingsInputData.getUpdatedUsername());
    roomsSettingsDataAccessObject.propogateDisplayNameChange(user, settingsInputData.getUpdatedUsername());

    
  }
}
