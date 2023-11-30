package use_case.settings;

import entity.User;

public class SettingsInputData {

    User user;

    public SettingsInputData(User user) {
        this.user = user;
    }

    User getUser() {
        return user;
    }
}
