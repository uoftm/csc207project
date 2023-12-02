package view;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class SignupView implements PropertyChangeListener {
  public static final String viewName = "sign up";

  private SignupController signupController;

  private JButton signUp;
  private JButton cancel;
  public JPanel contentPane;
  private JTextField email;
  private JTextField username;
  private JPasswordField password;
  private JPasswordField repeatPassword;
  private JPanel body;

  public SignupView(
      SignupController controller,
      SignupViewModel signupViewModel,
      SwitchViewController switchViewController) {
    this.signupController = controller;
    signupViewModel.addPropertyChangeListener(this);

    contentPane.setBackground(ViewConstants.background);
    contentPane.setPreferredSize(ViewConstants.windowSize);
    body.setBackground(ViewConstants.panel);
    body.setPreferredSize(ViewConstants.paneSize);

    signUp.addActionListener(
        evt -> {
          if (evt.getSource().equals(signUp)) {
            SignupState currentState = signupViewModel.getState();

            signupController.execute(
                currentState.getEmail(),
                currentState.getUsername(),
                currentState.getPassword(),
                currentState.getRepeatPassword());
          }
        });

    cancel.addActionListener(
        evt -> {
          if (evt.getSource().equals(cancel)) {
            switchViewController.switchTo(WelcomeView.viewName);
          }
        });

    email.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            SignupState currentState = signupViewModel.getState();
            String text = email.getText() + e.getKeyChar();
            currentState.setEmail(text);
            signupViewModel.setState(currentState);
          }

          @Override
          public void keyPressed(KeyEvent e) {}

          @Override
          public void keyReleased(KeyEvent e) {}
        });

    username.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            SignupState currentState = signupViewModel.getState();
            String text = username.getText() + e.getKeyChar();
            currentState.setUsername(text);
            signupViewModel.setState(currentState);
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
            SignupState currentState = signupViewModel.getState();
            currentState.setPassword(String.valueOf(password.getPassword()) + e.getKeyChar());
            signupViewModel.setState(currentState);
          }

          @Override
          public void keyPressed(KeyEvent e) {}

          @Override
          public void keyReleased(KeyEvent e) {}
        });

    repeatPassword.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            SignupState currentState = signupViewModel.getState();
            currentState.setRepeatPassword(
                String.valueOf(repeatPassword.getPassword()) + e.getKeyChar());
            signupViewModel.setState(currentState);
          }

          @Override
          public void keyPressed(KeyEvent e) {}

          @Override
          public void keyReleased(KeyEvent e) {}
        });
  }

  public JButton getSignupButton() {
    return signUp;
  }

  public JButton getCancelButton() {
    return cancel;
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    SignupState state = (SignupState) evt.getNewValue();
    if (state.getError() != null) {
      JOptionPane.showMessageDialog(contentPane, state.getError());
    }
  }
}
