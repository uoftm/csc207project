package interface_adapter.login;

public class LoginState {
  private String email = "";
  private String error = null;
  private String password = "";

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
