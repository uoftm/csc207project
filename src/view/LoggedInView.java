package view;

import entities.auth.User;
import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.settings.StartSettingsController;
import interface_adapter.switch_view.SwitchViewController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class LoggedInView implements PropertyChangeListener {

  public static final String viewName = "logged in";
  private final LoggedInViewModel loggedInViewModel;

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
      SwitchViewController switchViewController,
      StartSettingsController startSettingsController) {
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
            User userToPass = loggedInViewModel.getState().getUser();
            startSettingsController.passUser(userToPass);
            switchViewController.switchTo(SettingsView.viewName);
          }
        });

    rooms.add(roomsView.contentPane);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    LoggedInState state = (LoggedInState) evt.getNewValue();
    username.setText(state.getUser().getName());
  }
}
