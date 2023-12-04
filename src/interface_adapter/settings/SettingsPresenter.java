package interface_adapter.settings;

import interface_adapter.search.SearchState;
import use_case.settings.SettingsOutputBoundary;
import use_case.settings.SettingsOutputData;

public class SettingsPresenter implements SettingsOutputBoundary {

  private final SettingsViewModel settingsViewModel;

  public SettingsPresenter(SettingsViewModel settingsViewModel) {
    this.settingsViewModel = settingsViewModel;
  }

  @Override
  public void prepareSuccessView(SettingsOutputData outputData) {
    // On success, switch back to the same view for now
    System.out.println("success");
  }

  @Override
  public void prepareFailView(SettingsOutputData outputData) {
    SettingsState settingsState = settingsViewModel.getState();
    settingsState.setError(outputData.getError());
    settingsState.setIsError(outputData.getIsError());
    settingsViewModel.firePropertyChanged();
  }
}
