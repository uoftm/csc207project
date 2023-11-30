package view;

import interface_adapter.logged_in.LoggedInState;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class LoggedInView implements PropertyChangeListener {

  public final String viewName = "logged in";
  private final LoggedInViewModel loggedInViewModel;

  private JLabel username;

  private JButton logoutButton;
  private JButton settingsButton;
  public JPanel contentPane;
  private JPanel chat;

  public LoggedInView(
      LoggedInViewModel loggedInViewModel,
      ChatView chatView,
      SwitchViewController switchViewController) {
    contentPane.setBackground(ViewConstants.background);

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

    chat.add(chatView.contentPane);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    LoggedInState state = (LoggedInState) evt.getNewValue();
    username.setText(state.getUsername());
  }
}
