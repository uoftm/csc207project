package view;

import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import java.util.Random;
import javax.swing.*;

public class WelcomeView {
  public static final String viewName = "initial welcome";

  public JPanel contentPane;
  private JLabel captionLabel; // This is now bound through the GUI designer
  private JButton login;
  private JButton signup;
  private JPanel body;

  public JButton getLoginButton() {
    return login;
  }

  public JButton getSignupButton() {
    return signup;
  }

  public WelcomeView(SwitchViewController switchViewController) {
    contentPane.setBackground(ViewConstants.background);
    contentPane.setPreferredSize(ViewConstants.windowSize);
    body.setBackground(ViewConstants.panel);
    body.setPreferredSize(ViewConstants.paneSize);

    // Randomly select a caption
    Random rand = new Random();
    String randomCaption = ViewConstants.captions[rand.nextInt(ViewConstants.captions.length)];

    captionLabel.setText(randomCaption);
    captionLabel.setFont(new Font("Serif", Font.BOLD, 12));
    captionLabel.setHorizontalAlignment(JLabel.CENTER);

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
