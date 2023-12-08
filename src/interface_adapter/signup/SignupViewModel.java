package interface_adapter.signup;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SignupViewModel extends ViewModel {
  private SignupState state = new SignupState();

  public void setState(SignupState state) {
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

  public SignupState getState() {
    return state;
  }
}
