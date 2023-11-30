package interface_adapter.settings;

import entity.User;

public class SettingsState {
    private User user;
    private String error = null;

    public SettingsState(SettingsState state) {
        user = state.user;
        error = state.error;
    }

    public SettingsState() {}

    public User getUser() {
        return user;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
