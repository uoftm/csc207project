package view;

import interface_adapter.ViewManagerModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.*;

public class ViewManager implements PropertyChangeListener {
  private final CardLayout cardLayout;
  private final JPanel views;

  public ViewManager(ViewManagerModel viewManagerModel) {
    cardLayout = new CardLayout();

    // The various View objects. Only one view is visible at a time.
    views = new JPanel(cardLayout);
    viewManagerModel.addPropertyChangeListener(this);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("view")) {
      String viewModelName = (String) evt.getNewValue();
      cardLayout.show(views, viewModelName);
    }
    if (evt.getPropertyName().equals("views")) {
      views.removeAll();
      var viewsAndNames = (List<ViewManagerModel.ViewAndName>) evt.getNewValue();
      for (ViewManagerModel.ViewAndName viewAndName : viewsAndNames) {
        views.add(viewAndName.view, viewAndName.name);
      }
    }
  }

  public JPanel getViews() {
    return views;
  }
}
