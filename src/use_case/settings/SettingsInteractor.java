package use_case.settings;

import entities.auth.AbstractUser;
import entities.auth.User;
import entities.settings.SettingsChangeResponse;
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
  public void executeChangeUsername(SettingsInputData settingsInputData) {

    userSettingsDataAccessObject.changeDisplayName(settingsInputData.getUser(), settingsInputData.getUpdatedUsername());
    roomsSettingsDataAccessObject.propogateDisplayNameChange(settingsInputData.getUser(), settingsInputData.getUpdatedUsername());
    SettingsOutputData settingsOutputData = new SettingsOutputData(SettingsChangeResponse())
    if (settingsOutputData.getResponse().getIsError()) {
      settingsPresenter.prepareFailView(settingsOutputData);
    } else {
      settingsPresenter.prepareSuccessView(settingsOutputData);
    }

    
  }
}
