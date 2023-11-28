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

  public WelcomeView(SwitchViewController switchViewController) {
    contentPane.setBackground(ViewConstants.background);
    contentPane.setPreferredSize(ViewConstants.windowSize);
    body.setBackground(ViewConstants.panel);
    body.setPreferredSize(ViewConstants.paneSize);

    // Array of captions
    String[] captions = {
      "Connect Effortlessly, Communicate Seamlessly.",
      "Where Conversations Come Alive.",
      "Redefining Conversation in the Digital Age.",
      "Speak Freely, Connect Deeply.",
      "Bringing Your Words to Life.",
      "Instant Messaging, Lasting Connections.",
      "Talk the Talk, Anywhere, Anytime.",
      "Your World, Closer with Every Message.",
      "Elevate Your Chat Experience.",
      "Chat Smarter, Not Harder.",
      "Messages that Matter.",
      "Beyond Texting: Experience the Difference.",
      "Crafting Connections, One Message at a Time.",
      "Unleash the Power of Conversation.",
      "Speak Easy, Connect Instantly.",
      "Where Messages Meet Magic.",
      "Chat Revolutionized.",
      "Feel Closer with Every Text.",
      "Innovating How You Communicate.",
      "The Art of Messaging, Redefined."
    };

    // Randomly select a caption
    Random rand = new Random();
    String randomCaption = captions[rand.nextInt(captions.length)];

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
