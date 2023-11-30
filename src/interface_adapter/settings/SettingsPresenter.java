package interface_adapter.settings;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import use_case.settings.SettingsOutputBoundary;
import use_case.settings.SettingsOutputData;
import use_case.switch_view.SwitchViewOutputBoundary;
import use_case.switch_view.SwitchViewOutputData;

public class SettingsPresenter implements SettingsOutputBoundary, SwitchViewOutputBoundary {

    private final SettingsViewModel settingsViewModel;
    private final LoggedInViewModel loggedInViewModel;
    private final ViewManagerModel viewManagerModel;

    public SettingsPresenter(
            ViewManagerModel viewManagerModel,
            LoggedInViewModel loggedInViewModel,
            SettingsViewModel settingsViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.settingsViewModel = settingsViewModel;
    }

    @Override
    public void prepareSuccessView(SettingsOutputData response) {
        // On success, switch back to the same view for now
        System.out.println("success");
        this.viewManagerModel.setActiveView(settingsViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(String error) {
        // On error, switch back to the same view for now
        System.out.println("error");
        this.viewManagerModel.setActiveView(settingsViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void present(SwitchViewOutputData outputData) {
        // Logic to handle view switching
        this.viewManagerModel.setActiveView(outputData.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }
}
