package view;

import javax.swing.*;

public class MessageView extends JPanel {
  public MessageView(String message) {
    JLabel tmp = new JLabel(message);
    this.add(tmp);
  }
}
