package view;

import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import java.util.Random;
import javax.swing.*;

/**
 * This class represents the Welcome view in the application. It displays a random welcome message,
 * along with buttons to navigate to the login and signup views.
 */
public class WelcomeView {

  /**
   * The name of the Welcome view/page in the application. This variable is used as a parameter when
   * calling the `switchTo` method in `SwitchViewController` to navigate to a specific view.
   */
  public static final String viewName = "initial welcome";

  /**
   * The content pane for the WelcomeView class. This JPanel is used to contain all the UI elements
   * for the Welcome view/page. It includes a caption label, login button, signup button, and body
   * panel. We add this JPanel to the JCardLayout in Main class.
   */
  public JPanel contentPane;

  private JLabel captionLabel;
  private JButton login;
  private JButton signup;
  private JPanel body;

  /**
   * Used for testing
   *
   * @return The login button from the Welcome view.
   */
  public JButton getLoginButton() {
    return login;
  }

  /**
   * Used for testing
   *
   * @return The signup button from the WelcomeView class.
   */
  public JButton getSignupButton() {
    return signup;
  }

  /**
   * Constructs a WelcomeView object with the given SwitchViewController.
   *
   * @param switchViewController the SwitchViewController used for navigation between views
   */
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
