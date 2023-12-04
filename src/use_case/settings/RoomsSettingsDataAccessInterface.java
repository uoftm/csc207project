package use_case.settings;

import entities.auth.User;

public interface RoomsSettingsDataAccessInterface {
    void propogateDisplayNameChange(User user, String newDisplayName);
}
