package interface_adapter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import javax.swing.*;

/**
 * The ViewManagerModel class is responsible for managing views within a JCardLayout. It provides
 * methods to set the active view and add new views.
 *
 * <p>It also supports property change events to notify listeners of any changes to the views.
 */
public class ViewManagerModel {
  /**
   * The ViewAndName class represents a combination of a JPanel view and its corresponding name. It
   * is used in the ViewManager to name each of the views
   */
  public static class ViewAndName {
    private final JPanel view;
    private final String name;

    /**
     * Creates a new instance of the ViewAndName class with the given view and name.
     *
     * @param view The JPanel view to add to the ViewManager
     * @param name The name of the view
     */
    public ViewAndName(JPanel view, String name) {
      this.view = view;
      this.name = name;
    }

    /**
     * @return The JPanel view object.
     */
    public JPanel getView() {
      return view;
    }

    /**
     * @return The name of the view
     */
    public String getName() {
      return name;
    }
  }

  private String activeViewName;
  private final ArrayList<ViewAndName> views = new ArrayList<>();

  private final PropertyChangeSupport support = new PropertyChangeSupport(this);

  /**
   * @return the current active view name.
   */
  public String getActiveView() {
    return activeViewName;
  }

  /**
   * Sets the active view to the specified view name.
   *
   * <p>This method updates the activeViewName field with the given view name, and fires a property
   * change event with the property name "view" with the new value
   *
   * @param activeView The name of the view to set as active
   */
  public void setActiveView(String activeView) {
    this.activeViewName = activeView;
    support.firePropertyChange("view", null, this.activeViewName);
  }

  /**
   * Adds a new view to the ViewManager.
   *
   * <p>This method creates a new instance of the ViewAndName class with the given view and name.
   * The new ViewAndName instance is added to the views list. Then, it fires a property change event
   * with the updated views list as the new value.
   *
   * @param view The JPanel view to add to the ViewManager
   * @param viewName The name of the view
   */
  public void add(JPanel view, String viewName) {
    this.views.add(new ViewAndName(view, viewName));
    support.firePropertyChange("views", null, this.views);
  }

  /**
   * Adds a PropertyChangeListener to the ViewManagerModel's PropertyChangeSupport.
   *
   * <p>When the property change event is fired, the listener will be notified.
   *
   * @param listener The PropertyChangeListener to add
   */
  public void addPropertyChangeListener(PropertyChangeListener listener) {
    support.addPropertyChangeListener(listener);
  }
}
