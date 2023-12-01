package entities.auth;

public interface PasswordValidator {
  boolean passwordIsValid(String password);
}
