package interface_adapter.logged_in;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class LoggedInViewModel extends ViewModel {

  private String loggedInUser;

  public LoggedInViewModel() {
    super("logged in");
  }

  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  // This is what the Login Presenter will call to let the ViewModel know
  // to alert the View
  public void firePropertyChanged() {
    support.firePropertyChange("state", null, null);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  public String getLoggedInUser() {
    return loggedInUser;
  }

  public void setLoggedInUser(String loggedInUser) {
    this.loggedInUser = loggedInUser;
  }
}
