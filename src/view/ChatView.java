package view;

import entities.Message;
import interface_adapter.chat.ChatController;
import interface_adapter.chat.ChatState;
import interface_adapter.chat.ChatViewModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.*;

public class ChatView implements PropertyChangeListener {
  public JPanel contentPane;
  private JPanel paneInternals;
  private JScrollPane scrollPane;
  private JTextField message;
  private JButton send;
  private JPanel rawPane;
  private final ChatViewModel viewModel;

  public ChatView(ChatController chatController, ChatViewModel viewModel) {
    this.viewModel = viewModel;
    this.viewModel.addPropertyChangeListener(this);

    rawPane.setLayout(new BoxLayout(rawPane, BoxLayout.Y_AXIS));

    paneInternals = new JPanel();
    paneInternals.setLayout(new BoxLayout(paneInternals, BoxLayout.Y_AXIS));

    JScrollPane scrollPane = new JScrollPane(paneInternals);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(300, 300));

    rawPane.add(scrollPane);

    send.addActionListener(
        evt -> {
          if (evt.getSource().equals(send)) {
            ChatState currentState = viewModel.getState();

            String messageText = currentState.getMessage();
            chatController.sendMessage(messageText);
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
