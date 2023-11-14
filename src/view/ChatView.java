package view;

import entity.Message;
import interface_adapter.chat.ChatController;
import interface_adapter.chat.ChatViewModel;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.*;

public class ChatView extends JPanel implements PropertyChangeListener {
  private final JPanel paneInternals;

  private final ChatViewModel viewModel;

  public ChatView(ChatController chatController, ChatViewModel viewModel) {
    this.viewModel = viewModel;
    this.viewModel.addPropertyChangeListener(this);

    paneInternals = new JPanel();
    paneInternals.setLayout(new BoxLayout(paneInternals, BoxLayout.Y_AXIS));

    JScrollPane scrollPane = new JScrollPane(paneInternals);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(300, 300));

    var messagePanel = new JPanel();
    var message = new JTextField(15);
    var send = new JButton("Send");
    messagePanel.add(message);
    messagePanel.add(send);
    this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    this.add(scrollPane);
    this.add(messagePanel);

    chatController.loadAllMessages();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    var state = (List<Message>) evt.getNewValue();
    for (var message : state) {
      var tmp = new MessageView(message.content);
      paneInternals.add(tmp);
    }
  }
}
