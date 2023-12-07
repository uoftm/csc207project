package interface_adapter.settings;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SettingsViewModel extends ViewModel {
  private SettingsState state = new SettingsState();

  public void setState(SettingsState state) {
    this.state = state;
  }

  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  // This is what the Settings Presenter will call to let the ViewModel know
  public void firePropertyChanged() {
    support.firePropertyChange("state", null, this.state);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  public SettingsState getState() {
    return state;
  }
}
