package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsPresenter;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.io.IOException;
import javax.swing.*;

import use_case.settings.SettingsDataAccessInterface;
import use_case.settings.SettingsInputBoundary;
import use_case.settings.SettingsInteractor;
import use_case.settings.SettingsOutputBoundary;
import view.SettingsView;

public class SettingsUseCaseFactory {

    private SettingsUseCaseFactory() {}

    public static SettingsView create(
            ViewManagerModel viewManagerModel,
            SettingsViewModel settingsViewModel,
            LoggedInViewModel loggedInViewModel,
            SettingsDataAccessInterface settingsDataAccessObject,
            SwitchViewController switchViewController) {

        try {
            SettingsController settingsController =
                    createSettingsController(
                            viewManagerModel, settingsViewModel, loggedInViewModel, settingsDataAccessObject);

            return new SettingsView(settingsViewModel, settingsController, switchViewController);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Not available.");
            return null;
        }
    }

    private static SettingsController createSettingsController(
            ViewManagerModel viewManagerModel,
            SettingsViewModel settingsViewModel,
            LoggedInViewModel loggedInViewModel,
            SettingsDataAccessInterface settingsDataAccessObject)
            throws IOException {

        SettingsOutputBoundary settingsOutputBoundary =
                new SettingsPresenter(viewManagerModel, loggedInViewModel, settingsViewModel);
        SettingsInputBoundary settingsInteractor =
                new SettingsInteractor(settingsOutputBoundary);

        return new SettingsController(settingsInteractor);
    }
}
