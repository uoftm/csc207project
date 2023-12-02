package use_case.settings;

import entities.auth.User;

public interface DeleteUserDataAccessInterface {
    void deleteUser(User user);
}
