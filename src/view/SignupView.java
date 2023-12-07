package view;

import interface_adapter.signup.SignupController;
import interface_adapter.signup.SignupState;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class SignupView implements PropertyChangeListener {
  public static final String viewName = "sign up";

  private final SignupController signupController;

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

    email
        .getDocument()
        .addDocumentListener(
            new DocumentUpdateListener() {
              public void update() {
                SignupState currentState = signupViewModel.getState();
                currentState.setEmail(email.getText());
                signupViewModel.setState(currentState);
              }
            });

    username
        .getDocument()
        .addDocumentListener(
            new DocumentUpdateListener() {
              public void update() {
                SignupState currentState = signupViewModel.getState();
                currentState.setUsername(username.getText());
                signupViewModel.setState(currentState);
              }
            });

    password
        .getDocument()
        .addDocumentListener(
            new DocumentUpdateListener() {
              @Override
              public void update() {
                SignupState currentState = signupViewModel.getState();
                currentState.setPassword(String.valueOf(password.getPassword()));
                signupViewModel.setState(currentState);
              }
            });

    repeatPassword
        .getDocument()
        .addDocumentListener(
            new DocumentUpdateListener() {
              @Override
              public void update() {
                SignupState currentState = signupViewModel.getState();
                currentState.setRepeatPassword(String.valueOf(repeatPassword.getPassword()));
                signupViewModel.setState(currentState);
              }
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
