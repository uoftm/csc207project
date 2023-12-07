package interface_adapter.settings;

import interface_adapter.logged_in.LoggedInViewModel;
import use_case.settings.SettingsOutputBoundary;
import use_case.settings.SettingsOutputData;

public class SettingsPresenter implements SettingsOutputBoundary {
  private final SettingsViewModel settingsViewModel;
  private final LoggedInViewModel loggedInViewModel;

  /**
   * SettingsPresenter Class
   *
   * <p>This class is responsible for handling the presentation logic of the settings view. It
   * receives the necessary view models, performs operations based on the input data, and updates
   * the view models accordingly. It implements the SettingsOutputBoundary interface.
   *
   * @param settingsViewModel The view model for the settings view
   * @param loggedInViewModel The view model for the logged-in user's view
   */
  public SettingsPresenter(
      SettingsViewModel settingsViewModel, LoggedInViewModel loggedInViewModel) {
    this.settingsViewModel = settingsViewModel;
    this.loggedInViewModel = loggedInViewModel;
  }

  /**
   * Prepares the success view after updating the username.
   *
   * <p>This method sets the logged-in user in the LoggedInViewModel, updates the settings state
   * with a success message, and notifies the related view models of the changes.
   *
   * @param outputData The data containing the updated username
   */
  @Override
  public void prepareSuccessView(SettingsOutputData outputData) {
    loggedInViewModel.setLoggedInUser(outputData.getUsername());
    loggedInViewModel.firePropertyChanged();
    SettingsState settingsState = settingsViewModel.getState();
    settingsState.setMessage("Successfully updated username");
    settingsState.setIsSuccess(true);
    settingsViewModel.firePropertyChanged();
  }

  /**
   * Prepares the fail view after encountering an error during the settings update.
   *
   * <p>This method is called to update the settings state with the error message and the error
   * status. It notifies the related view models of the changes.
   *
   * @param outputData The data containing the error message and status
   */
  @Override
  public void prepareFailView(SettingsOutputData outputData) {
    SettingsState settingsState = settingsViewModel.getState();
    settingsState.setMessage(outputData.getError());
    settingsState.setIsError(outputData.getIsError());
    settingsViewModel.firePropertyChanged();
  }
}
