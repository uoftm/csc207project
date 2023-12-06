package use_case.settings;

import entities.auth.User;
import use_case.rooms.LoggedInDataAccessInterface;

public class SettingsInteractor implements SettingsInputBoundary {
  final SettingsOutputBoundary settingsPresenter;

  final UserSettingsDataAccessInterface userSettingsDataAccessObject;
  final RoomsSettingsDataAccessInterface roomsSettingsDataAccessObject;
  final LoggedInDataAccessInterface inMemoryDAO;

  public SettingsInteractor(
      UserSettingsDataAccessInterface userSettingsDataAccessInterface,
      RoomsSettingsDataAccessInterface roomsSettingsDataAccessInterface,
      LoggedInDataAccessInterface inMemoryDAO,
      SettingsOutputBoundary settingsOutputBoundary) {
    this.userSettingsDataAccessObject = userSettingsDataAccessInterface;
    this.roomsSettingsDataAccessObject = roomsSettingsDataAccessInterface;
    this.settingsPresenter = settingsOutputBoundary;
    this.inMemoryDAO = inMemoryDAO;
  }

  @Override
  public void executeChangeUsername(SettingsInputData settingsInputData) {
    try {
      User user = inMemoryDAO.getUser();
      user.setName(settingsInputData.getNewUsername());
      inMemoryDAO.setUser(user);
      String idToken = inMemoryDAO.getIdToken();
      userSettingsDataAccessObject.propogateDisplayNameChange(idToken, user);
      roomsSettingsDataAccessObject.propogateDisplayNameChange(idToken, user);
      SettingsOutputData settingsOutputData = new SettingsOutputData(null, user.getName());
      settingsPresenter.prepareSuccessView(settingsOutputData);
    } catch (RuntimeException e) {
      SettingsOutputData settingsOutputData = new SettingsOutputData(e.getMessage(), null);
      settingsPresenter.prepareFailView(settingsOutputData);
    }
  }
}
