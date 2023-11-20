package view;

import entity.Message;
import interface_adapter.chat.ChatController;
import interface_adapter.chat.ChatState;
import interface_adapter.chat.ChatViewModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
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

    send.addActionListener(
        // This creates an anonymous subclass of ActionListener and instantiates it.
        new ActionListener() {
          public void actionPerformed(ActionEvent evt) {
            if (evt.getSource().equals(send)) {
              ChatState currentState = viewModel.getState();

              String messageText = currentState.getMessage();
              chatController.sendMessage(messageText);
            }
          }
        });
    message.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            ChatState currentState = viewModel.getState();
            String text = message.getText() + e.getKeyChar();
            currentState.setMessage(text);
            viewModel.setState(currentState);
          }

          @Override
          public void keyPressed(KeyEvent e) {}

          @Override
          public void keyReleased(KeyEvent e) {}
        });

    chatController.loadAllMessages();
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    var state = (List<Message>) evt.getNewValue();
    for (var message : state) {
      var messageView = new MessageView(message.content);
      paneInternals.add(messageView);
    }
  }
}
