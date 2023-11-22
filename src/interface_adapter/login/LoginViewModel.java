package interface_adapter.login;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LoginViewModel extends ViewModel {

  public static final String TITLE_LABEL = "Login Screen";
  public static final String EMAIL_LABEL = "Email";
  public static final String PASSWORD_LABEL = "Password";

  public static final String LOGIN_BUTTON_LABEL = "Log in";
  public static final String CANCEL_BUTTON_LABEL = "Cancel";

  private LoginState state = new LoginState();

  public LoginViewModel() {
    super("log in");
  }

  public void setState(LoginState state) {
    this.state = state;
  }

  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  // This is what the Signup Presenter will call to let the ViewModel know
  // to alert the View
  public void firePropertyChanged() {
    support.firePropertyChange("state", null, this.state);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  public LoginState getState() {
    return state;
  }
}
