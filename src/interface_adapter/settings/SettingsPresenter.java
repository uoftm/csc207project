package interface_adapter.settings;

import interface_adapter.logged_in.LoggedInViewModel;
import use_case.settings.SettingsOutputBoundary;
import use_case.settings.SettingsOutputData;

public class SettingsPresenter implements SettingsOutputBoundary {

  private final SettingsViewModel settingsViewModel;
  private final LoggedInViewModel loggedInViewModel;

  public SettingsPresenter(
      SettingsViewModel settingsViewModel, LoggedInViewModel loggedInViewModel) {
    this.settingsViewModel = settingsViewModel;
    this.loggedInViewModel = loggedInViewModel;
  }

  @Override
  public void prepareSuccessView(SettingsOutputData outputData) {
    loggedInViewModel.firePropertyChanged();
    SettingsState settingsState = settingsViewModel.getState();
    settingsState.setMessage("Successfully updated username");
    settingsState.setIsSuccess(true);
    settingsViewModel.firePropertyChanged();
  }

  @Override
  public void prepareFailView(SettingsOutputData outputData) {
    SettingsState settingsState = settingsViewModel.getState();
    settingsState.setMessage(outputData.getError());
    settingsState.setIsError(outputData.getIsError());
    settingsViewModel.firePropertyChanged();
  }
}
