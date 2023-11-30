package interface_adapter.settings;

import use_case.settings.SettingsOutputBoundary;
import use_case.settings.SettingsOutputData;
import use_case.switch_view.SwitchViewOutputBoundary;
import use_case.switch_view.SwitchViewOutputData;

public class SettingsPresenter implements SettingsOutputBoundary, SwitchViewOutputBoundary {

  private final SettingsViewModel settingsViewModel;

  public SettingsPresenter(SettingsViewModel settingsViewModel) {
    this.settingsViewModel = settingsViewModel;
  }

  @Override
  public void prepareSuccessView(SettingsOutputData response) {
    // On success, switch back to the same view for now
    System.out.println("success");
  }

  @Override
  public void prepareFailView(String error) {
    // On error, switch back to the same view for now
    System.out.println("error");
  }

  @Override
  public void present(SwitchViewOutputData outputData) {
    // Logic to handle view switching
  }
}
