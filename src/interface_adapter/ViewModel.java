package interface_adapter;

import java.beans.PropertyChangeListener;

/**
 * The ViewModel class represents the base class for all view models in the application. It provides
 * a generic interface for firing property change events and adding property change listeners.
 */
public abstract class ViewModel {
  /**
   * Notifies listeners that a property has changed. Subclasses must implement this method to handle
   * specific property change events.
   */
  public abstract void firePropertyChanged();

  /**
   * Adds a {@link PropertyChangeListener} to the list of listeners for property change events. This
   * will be triggered any time the ViewModel state is changed
   *
   * @param listener the listener to be added
   */
  public abstract void addPropertyChangeListener(PropertyChangeListener listener);
}
