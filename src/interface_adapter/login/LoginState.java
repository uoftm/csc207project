package interface_adapter.login;

public class LoginState {
  private String email = "";
  private String error = null;
  private String password = "";

  // Because of the previous copy constructor, the default constructor must be explicit.
  public LoginState() {}

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public String getError() {
    return error;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setError(String error) {
    this.error = error;
  }
}
