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

  private JButton logOut;
  public JPanel contentPane;
  private JPanel chat;

  /** A window with a title and a JButton. */
  public LoggedInView(
      LoggedInViewModel loggedInViewModel,
      ChatView chatView,
      SwitchViewController switchViewController) {
    contentPane.setBackground(ViewConstants.background);

    this.loggedInViewModel = loggedInViewModel;
    this.loggedInViewModel.addPropertyChangeListener(this);

    logOut.addActionListener(
        evt -> {
          if (evt.getSource().equals(logOut)) {
            switchViewController.switchTo(LoginView.viewName);
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
