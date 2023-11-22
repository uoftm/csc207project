package view;

import interface_adapter.login.LoginController;
import interface_adapter.login.LoginState;
import interface_adapter.login.LoginViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class LoginView extends JPanel implements PropertyChangeListener {

  public static final String viewName = "log in";
  private final LoginViewModel loginViewModel;

  final JTextField emailInputField = new JTextField(15);
  private final JLabel emailErrorField = new JLabel();

  final JPasswordField passwordInputField = new JPasswordField(15);
  private final JLabel passwordErrorField = new JLabel();

  final JButton logIn;
  final JButton cancel;
  private final LoginController loginController;

  public LoginView(
      LoginViewModel loginViewModel,
      LoginController controller,
      SwitchViewController switchViewController) {
    this.setBackground(ViewConstants.background);

    this.loginController = controller;
    this.loginViewModel = loginViewModel;
    this.loginViewModel.addPropertyChangeListener(this);

    JPanel body = new JPanel();
    body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
    // It seems the y-axis is ignored here
    body.setMaximumSize(ViewConstants.paneSize);
    body.setBackground(ViewConstants.panel);
    body.setAlignmentX(CENTER_ALIGNMENT);
    body.setAlignmentY(CENTER_ALIGNMENT);

    JLabel title = new JLabel(LoginViewModel.TITLE_LABEL);
    title.setAlignmentX(Component.CENTER_ALIGNMENT);

    LabelTextPanel emailInfo = new LabelTextPanel(new JLabel(LoginViewModel.EMAIL_LABEL), emailInputField);
    LabelTextPanel passwordInfo = new LabelTextPanel(new JLabel(LoginViewModel.PASSWORD_LABEL), passwordInputField);
    emailInfo.setBackground(body.getBackground());
    passwordInfo.setBackground(body.getBackground());

    JPanel buttons = new JPanel();
    buttons.setBackground(body.getBackground());
    logIn = new JButton(LoginViewModel.LOGIN_BUTTON_LABEL);
    buttons.add(logIn);
    cancel = new JButton(LoginViewModel.CANCEL_BUTTON_LABEL);
    buttons.add(cancel);

    logIn.addActionListener(
        // This creates an anonymous subclass of ActionListener and instantiates it.
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

    emailInputField.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            LoginState currentState = loginViewModel.getState();
            currentState.setEmail(emailInputField.getText() + e.getKeyChar());
            loginViewModel.setState(currentState);
          }

          @Override
          public void keyPressed(KeyEvent e) {}

          @Override
          public void keyReleased(KeyEvent e) {}
        });
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    passwordInputField.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            LoginState currentState = loginViewModel.getState();
            currentState.setPassword(passwordInputField.getText() + e.getKeyChar());
            loginViewModel.setState(currentState);
          }

          @Override
          public void keyPressed(KeyEvent e) {}

          @Override
          public void keyReleased(KeyEvent e) {}
        });

    body.add(title);
    body.add(emailInfo);
    body.add(emailErrorField);
    body.add(passwordInfo);
    body.add(passwordErrorField);
    body.add(buttons);

    this.add(Box.createGlue());
    this.add(body);
    this.add(Box.createGlue());

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setPreferredSize(ViewConstants.windowSize);
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    LoginState state = (LoginState) evt.getNewValue();
    setFields(state);
  }

  private void setFields(LoginState state) {
    emailInputField.setText(state.getEmail());
  }
}
