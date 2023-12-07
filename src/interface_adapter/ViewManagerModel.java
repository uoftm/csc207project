package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import javax.swing.*;

public class ViewManagerModel {
  public static class ViewAndName {
    public final JPanel view;
    public final String name;

    public ViewAndName(JPanel view, String name) {
      this.view = view;
      this.name = name;
    }
  }

  private String activeViewName;
  private final ArrayList<ViewAndName> views = new ArrayList<>();

  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  public String getActiveView() {
    return activeViewName;
  }

  public void setActiveView(String activeView) {
    this.activeViewName = activeView;
    support.firePropertyChange("view", null, this.activeViewName);
  }

  public void add(JPanel view, String viewName) {
    this.views.add(new ViewAndName(view, viewName));
    support.firePropertyChange("views", null, this.views);
  }

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }
}
