package view;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

public class MessageView extends JPanel {
  public MessageView(String message, String displayName) {
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

    JLabel labelDisplayName = new JLabel(displayName);
    JLabel labelMessage = new JLabel(message);

    Font displayNameFont = new Font(labelDisplayName.getFont().getName(), Font.PLAIN, 12);
    Font messageFont = new Font(labelMessage.getFont().getName(), Font.PLAIN, 14);
    labelDisplayName.setFont(displayNameFont);
    labelMessage.setFont(messageFont);

    labelMessage.setForeground(new Color(50, 50, 50));

    Border bottomMargin = BorderFactory.createEmptyBorder(0, 0, 5, 0);

    labelMessage.setBorder(bottomMargin);
    this.add(labelDisplayName);
    this.add(labelMessage);
  }
}
