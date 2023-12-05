package interface_adapter.settings;

import entities.auth.User;

public class StartSettingsController {
    // This controller follows the MVC architecture, as it needs to only change external files
    // See https://piazza.com/class/lm84a7mo56k5wy/post/1231 for details on why we aren't following Clean Architecture here
    private final SettingsViewModel settingsViewModel;

    public StartSettingsController(SettingsViewModel settingsViewModel) {
        this.settingsViewModel = settingsViewModel;
    }

    public void passUser(User user) {
        settingsViewModel.getState().setUser(user);
    }
}

