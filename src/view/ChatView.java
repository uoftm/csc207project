package view;

import java.awt.*;
import javax.swing.*;

public class ChatView extends JPanel {

  public ChatView() {
    JPanel paneInternals = new JPanel();
    paneInternals.setLayout(new BoxLayout(paneInternals, BoxLayout.Y_AXIS));

    JScrollPane scrollPane = new JScrollPane(paneInternals);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(300, 300));

    for (var i = 0; i < 100; i++) {
      var tmp = new MessageView("This is a message " + i);
      paneInternals.add(tmp);
    }

    var messagePanel = new JPanel();
    var message = new JTextField(15);
    var send = new JButton("Send");
    messagePanel.add(message);
    messagePanel.add(send);
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(scrollPane);
    this.add(messagePanel);
  }
}
