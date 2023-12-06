package use_case.settings;


import use_case.login.LoginUserDataAccessInterface;

public class SettingsInteractor implements SettingsInputBoundary {
  // TODO: Add this after other code is merged
  // final SettingsUserDataAccessInterface settingsUserDataAccessInterface;
  final SettingsOutputBoundary settingsPresenter;

  final UserSettingsDataAccessInterface userSettingsDataAccessObject;
  final RoomsSettingsDataAccessInterface roomsSettingsDataAccessObject;
  final LoginUserDataAccessInterface userDao;

  public SettingsInteractor(
      UserSettingsDataAccessInterface userSettingsDataAccessInterface,
      RoomsSettingsDataAccessInterface roomsSettingsDataAccessInterface,
      LoginUserDataAccessInterface userDao,
      SettingsOutputBoundary settingsOutputBoundary) {
    this.userSettingsDataAccessObject = userSettingsDataAccessInterface;
    this.roomsSettingsDataAccessObject = roomsSettingsDataAccessInterface;
    this.userDao = userDao;
    this.settingsPresenter = settingsOutputBoundary;
  }

  @Override
  public void executeChangeUsername(SettingsInputData settingsInputData) {
    try {
      // TODO: Actually change the name
      userSettingsDataAccessObject.propogateDisplayNameChange(
              settingsInputData.getUser());
      roomsSettingsDataAccessObject.propogateDisplayNameChange(
              settingsInputData.getUser(), userDao);
      SettingsOutputData settingsOutputData = new SettingsOutputData(null, false);
      settingsPresenter.prepareSuccessView(settingsOutputData);
    } catch (RuntimeException e) {
      SettingsOutputData settingsOutputData = new SettingsOutputData(e.getMessage(), true);
      settingsPresenter.prepareFailView(settingsOutputData);
    }
  }
}
