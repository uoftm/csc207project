package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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
        new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            if (evt.getSource().equals(logIn)) {
              LoginState currentState = loginViewModel.getState();

              loginController.execute(currentState.getEmail(), currentState.getPassword());
            }
          }
        });

    cancel.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            if (evt.getSource().equals(cancel)) {
              switchViewController.switchTo(WelcomeView.viewName);
            }
          }
        });

    email.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            LoginState currentState = loginViewModel.getState();
            currentState.setEmail(email.getText() + e.getKeyChar());
            loginViewModel.setState(currentState);
          }

          @Override
          public void keyPressed(KeyEvent e) {}

          @Override
          public void keyReleased(KeyEvent e) {}
        });

    password.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            LoginState currentState = loginViewModel.getState();
            currentState.setPassword(password.getText() + e.getKeyChar());
            loginViewModel.setState(currentState);
          }

          @Override
          public void keyPressed(KeyEvent e) {}

          @Override
          public void keyReleased(KeyEvent e) {}
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
