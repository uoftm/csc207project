package interface_adapter.searched;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SearchedViewModel extends ViewModel {

  private SearchedState state = new SearchedState();

  public void setState(SearchedState state) {
    this.state = state;
  }

  public SearchedState getState() {
    return state;
  }

  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  @Override
  public void firePropertyChanged() {
    support.firePropertyChange("state", null, state);
  }

  @Override
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }
}
