package view;

import interface_adapter.logged_in.LoggedInController;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class LoggedInView implements PropertyChangeListener {

  public static final String viewName = "logged in";
  private final LoggedInViewModel loggedInViewModel;
  private final LoggedInController loggedInController;

  private JLabel username;

  private JButton logoutButton;
  private JButton settingsButton;
  public JPanel contentPane;
  private JPanel nav;
  private JPanel navBody;
  private JPanel navMiddle;
  private JPanel rooms;
  private JPanel panel1;

  public LoggedInView(
      LoggedInViewModel loggedInViewModel,
      RoomsView roomsView,
      LoggedInController loggedInController,
      SwitchViewController switchViewController) {
    contentPane.setBackground(ViewConstants.background);
    contentPane.setPreferredSize(ViewConstants.windowSize);

    nav.setBackground(ViewConstants.panel);
    nav.setPreferredSize(ViewConstants.navSize);
    navBody.setBackground(ViewConstants.panel);
    navMiddle.setBackground(ViewConstants.background);
    navMiddle.setPreferredSize(ViewConstants.navMiddle);

    this.loggedInViewModel = loggedInViewModel;
    this.loggedInViewModel.addPropertyChangeListener(this);

    logoutButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(logoutButton)) {
            switchViewController.switchTo(LoginView.viewName);
          }
        });

    settingsButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(settingsButton)) {
            switchViewController.switchTo(SettingsView.viewName);
          }
        });

    rooms.add(roomsView.contentPane);
    this.loggedInController = loggedInController;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    loggedInController.execute();
  }
}
