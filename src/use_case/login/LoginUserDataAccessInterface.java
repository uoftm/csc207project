package use_case.login;

import entity.User;

public interface LoginUserDataAccessInterface {
  User get(String email, String password);
}
