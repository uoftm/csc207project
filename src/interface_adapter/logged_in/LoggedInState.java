package interface_adapter.logged_in;

public class LoggedInState {
  private String username = "";

  /**
   * @return the username that the user is currently typing in to log in
   */
  public String getUsername() {
    return username;
  }

  /**
   * @param username the new username that the user has typed on the login screen
   */
  public void setUsername(String username) {
    this.username = username;
  }
}
