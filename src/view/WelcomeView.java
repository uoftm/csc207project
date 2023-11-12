package view;

import interface_adapter.ViewManagerModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class WelcomeView extends JPanel {
  public static final String viewName = "initial welcome";
  private final JButton login;
  private final JButton signUp;

  public WelcomeView(ViewManagerModel viewManagerModel) {
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
              viewManagerModel.setActiveView(SignupView.viewName);
              viewManagerModel.firePropertyChanged();
            }
          }
        });

    login.addActionListener(
        new ActionListener() {
          @Override
          public void actionPerformed(ActionEvent evt) {
            if (evt.getSource().equals(login)) {
              viewManagerModel.setActiveView(LoginView.viewName);
              viewManagerModel.firePropertyChanged();
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
