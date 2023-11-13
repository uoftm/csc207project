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

  final JTextField usernameInputField = new JTextField(15);
  private final JLabel usernameErrorField = new JLabel();

  final JPasswordField passwordInputField = new JPasswordField(15);
  private final JLabel passwordErrorField = new JLabel();

  final JButton logIn;
  final JButton cancel;
  private final LoginController loginController;
  private final SwitchViewController switchViewController;

  public LoginView(
      LoginViewModel loginViewModel,
      LoginController controller,
      SwitchViewController switchViewController) {
    this.setBackground(Colors.background);

    this.loginController = controller;
    this.loginViewModel = loginViewModel;
    this.loginViewModel.addPropertyChangeListener(this);
    this.switchViewController = switchViewController;

    JPanel body = new JPanel();
    body.setBackground(Colors.panel);
    body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
    // It seems the y-axis is ignored here
    body.setMaximumSize(new Dimension(400, 300));
    body.setAlignmentX(CENTER_ALIGNMENT);
    body.setAlignmentY(CENTER_ALIGNMENT);

    JLabel title = new JLabel("Login Screen");
    title.setBackground(Colors.panel);
    title.setAlignmentX(Component.CENTER_ALIGNMENT);

    LabelTextPanel usernameInfo = new LabelTextPanel(new JLabel("Username"), usernameInputField);
    LabelTextPanel passwordInfo = new LabelTextPanel(new JLabel("Password"), passwordInputField);
    usernameInfo.setBackground(body.getBackground());
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

              loginController.execute(currentState.getUsername(), currentState.getPassword());
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

    usernameInputField.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            LoginState currentState = loginViewModel.getState();
            currentState.setUsername(usernameInputField.getText() + e.getKeyChar());
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
    body.add(usernameInfo);
    body.add(usernameErrorField);
    body.add(passwordInfo);
    body.add(passwordErrorField);
    body.add(buttons);

    this.add(Box.createGlue());
    this.add(body);
    this.add(Box.createGlue());

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setPreferredSize(new Dimension(800, 600));
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    LoginState state = (LoginState) evt.getNewValue();
    setFields(state);
  }

  private void setFields(LoginState state) {
    usernameInputField.setText(state.getUsername());
  }
}
