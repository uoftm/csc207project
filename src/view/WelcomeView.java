package view;

import interface_adapter.ViewManagerModel;
import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class WelcomeView extends JPanel {
  public static final String viewName = "initial welcome";
  private final JButton login;
  private final JButton signUp;

  public WelcomeView(ViewManagerModel viewManagerModel, SwitchViewController switchViewController) {
    this.setBackground(Colors.background);

    JPanel body = new JPanel();
    body.setBackground(Colors.panel);
    body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));
    // It seems the y-axis is ignored here
    body.setMaximumSize(new Dimension(400, 300));
    body.setAlignmentX(CENTER_ALIGNMENT);
    body.setAlignmentY(CENTER_ALIGNMENT);

    JLabel title = new JLabel("Welcome to MSGR!");
    title.setAlignmentX(Component.CENTER_ALIGNMENT);

    login = new JButton("Log In");
    signUp = new JButton("Sign Up");

    signUp.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            if (evt.getSource().equals(signUp)) {
              switchViewController.switchTo(SignupView.viewName);
            }
          }
        });

    login.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            if (evt.getSource().equals(login)) {
              switchViewController.switchTo(LoginView.viewName);
            }
          }
        });

    body.add(title);
    body.add(login);
    body.add(signUp);

    this.add(Box.createGlue());
    this.add(body);
    this.add(Box.createGlue());

    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.setPreferredSize(new Dimension(800, 600));
  }
}
