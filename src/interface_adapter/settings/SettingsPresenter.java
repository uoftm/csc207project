package interface_adapter.settings;

import use_case.settings.SettingsOutputBoundary;
import use_case.settings.SettingsOutputData;

public class SettingsPresenter implements SettingsOutputBoundary {

  private final SettingsViewModel settingsViewModel;

  public SettingsPresenter(SettingsViewModel settingsViewModel) {
    this.settingsViewModel = settingsViewModel;
  }

  @Override
  public void prepareSuccessView(SettingsOutputData outputData) {
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
