package interface_adapter.rooms;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class RoomsViewModel extends ViewModel {
  private RoomsState state = new RoomsState();

  public void setState(RoomsState state) {
    this.state = state;
  }

  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  public void firePropertyChanged() {
    support.firePropertyChange("state", null, this.state);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  public RoomsState getState() {
    return state;
  }
}
