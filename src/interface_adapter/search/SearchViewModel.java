package interface_adapter.search;

import interface_adapter.ViewModel;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class SearchViewModel extends ViewModel {

  private String highlightedText;
  private SearchState state = new SearchState();

  public SearchViewModel() {
    super("search");
  }

  public void setState(SearchState state) {
    this.state = state;
  }

  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  // This is what the Search Presenter will call to let the ViewModel know
  public void firePropertyChanged() {
    support.firePropertyChange("state", null, this.state);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }

  public SearchState getState() {
    return state;
  }
}
