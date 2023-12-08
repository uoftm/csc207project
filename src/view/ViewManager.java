package view;

import interface_adapter.ViewManagerModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.*;

/**
 * The ViewManager class is responsible for managing a set of views within a JPanel using a
 * CardLayout.
 */
public class ViewManager implements PropertyChangeListener {
  private final CardLayout cardLayout;
  private final JPanel views;

  /**
   * The ViewManager class is responsible for managing a set of views within a JPanel using a
   * CardLayout. It listens for property change events from a ViewManagerModel to update the active
   * view and the available views.
   *
   * @param viewManagerModel The ViewManagerModel to listen for property change events from.
   */
  public ViewManager(ViewManagerModel viewManagerModel) {
    cardLayout = new CardLayout();

    // The various View objects. Only one view is visible at a time.
    views = new JPanel(cardLayout);
    viewManagerModel.addPropertyChangeListener(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    // If the active view changes, update the active view
    if (evt.getPropertyName().equals("view")) {
      String viewModelName = (String) evt.getNewValue();
      cardLayout.show(views, viewModelName);
    }
    // If the available views change, clear the old views, and add the new views
    if (evt.getPropertyName().equals("views")) {
      views.removeAll();
      var viewsAndNames = (List<ViewManagerModel.ViewAndName>) evt.getNewValue();
      for (ViewManagerModel.ViewAndName viewAndName : viewsAndNames) {
        views.add(viewAndName.getView(), viewAndName.getName());
      }
    }
  }

  /**
   * The list of all views that are displayable (to be added to the CardLayout).
   *
   * @return The JPanel containing the views.
   */
  public JPanel getViews() {
    return views;
  }
}
