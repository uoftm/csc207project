package use_case.settings;

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
    try {
      userSettingsDataAccessObject.changeDisplayName(
          settingsInputData.getUser(), settingsInputData.getUpdatedUsername());
      roomsSettingsDataAccessObject.propogateDisplayNameChange(
          settingsInputData.getUser(), settingsInputData.getUpdatedUsername());
      SettingsOutputData settingsOutputData = new SettingsOutputData(null, false);
      settingsPresenter.prepareSuccessView(settingsOutputData);
    } catch (RuntimeException e) {
      SettingsOutputData settingsOutputData = new SettingsOutputData(e.getMessage(), true);
      settingsPresenter.prepareFailView(settingsOutputData);
    }
  }
}
