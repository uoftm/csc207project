package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class LoginView implements PropertyChangeListener {

  public static final String viewName = "log in";
  private final LoginViewModel loginViewModel;

  private JButton logIn;
  private JButton cancel;
  private final LoginController loginController;
  public JPanel contentPane;
  private JPanel body;
  private JTextField email;
  private JPasswordField password;

  public LoginView(
      LoginViewModel loginViewModel,
      LoginController controller,
      SwitchViewController switchViewController) {
    this.loginController = controller;
    this.loginViewModel = loginViewModel;
    this.loginViewModel.addPropertyChangeListener(this);

    contentPane.setBackground(ViewConstants.background);
    contentPane.setPreferredSize(ViewConstants.windowSize);
    body.setBackground(ViewConstants.panel);
    body.setPreferredSize(ViewConstants.paneSize);

    logIn.addActionListener(
        evt -> {
          if (evt.getSource().equals(logIn)) {
            LoginState currentState = loginViewModel.getState();

            loginController.execute(currentState.getEmail(), currentState.getPassword());
          }
        });

    cancel.addActionListener(
        evt -> {
          if (evt.getSource().equals(cancel)) {
            switchViewController.switchTo(WelcomeView.viewName);
          }
        });

    email
        .getDocument()
        .addDocumentListener(
            new DocumentUpdateListener() {

              public void update() {
                LoginState currentState = loginViewModel.getState();
                currentState.setEmail(email.getText());
                loginViewModel.setState(currentState);
              }
            });

    password
        .getDocument()
        .addDocumentListener(
            new DocumentUpdateListener() {
              @Override
              public void update() {
                LoginState currentState = loginViewModel.getState();
                currentState.setPassword(password.getText());
                loginViewModel.setState(currentState);
              }
            });
  }

  public JButton getLoginButton() {
    return logIn;
  }

  public JButton getCancelButton() {
    return cancel;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    LoginState state = (LoginState) evt.getNewValue();
    if (state.getError() != null) {
      JOptionPane.showMessageDialog(contentPane, state.getError());
    }
  }
}
