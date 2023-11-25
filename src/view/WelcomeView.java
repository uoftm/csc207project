package view;

import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import javax.swing.*;

public class WelcomeView {
  public static final String viewName = "initial welcome";

  public JPanel contentPane;

  private JButton login;
  private JButton signup;
  private JPanel body;

  public WelcomeView(SwitchViewController switchViewController) {
    contentPane.setBackground(ViewConstants.background);
    contentPane.setPreferredSize(ViewConstants.windowSize);
    body.setBackground(ViewConstants.panel);
    body.setPreferredSize(ViewConstants.paneSize);

    signup.addActionListener(
        evt -> {
          if (evt.getSource().equals(signup)) {
            switchViewController.switchTo(SignupView.viewName);
          }
        });

    login.addActionListener(
        evt -> {
          if (evt.getSource().equals(login)) {
            switchViewController.switchTo(LoginView.viewName);
          }
        });
  }
}
