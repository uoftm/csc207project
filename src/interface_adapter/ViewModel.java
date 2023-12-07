package interface_adapter;

import java.beans.PropertyChangeListener;

public abstract class ViewModel {
  public abstract void firePropertyChanged();

  public abstract void addPropertyChangeListener(PropertyChangeListener listener);
}
