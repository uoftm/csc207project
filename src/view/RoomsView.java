package view;

import entities.auth.User;
import entities.rooms.Message;
import interface_adapter.rooms.RoomsController;
import interface_adapter.rooms.RoomsState;
import interface_adapter.rooms.RoomsViewModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;
import javax.swing.*;

public class RoomsView implements PropertyChangeListener {
  public JPanel contentPane;
  private JPanel paneInternals;
  private JScrollPane scrollPane;
  private JTextField messageTextField;
  private JButton send;
  private JPanel rawPane;
  private JLabel roomNameLabel;
  private JButton changeRoomButton;
  private JButton refreshButton;
  private final RoomsViewModel viewModel;

  public RoomsView(RoomsViewModel viewModel, RoomsController roomsController) {
    this.viewModel = viewModel;
    this.viewModel.addPropertyChangeListener(this);

    rawPane.setLayout(new BoxLayout(rawPane, BoxLayout.Y_AXIS));

    paneInternals = new JPanel();
    paneInternals.setLayout(new BoxLayout(paneInternals, BoxLayout.Y_AXIS));

    JScrollPane scrollPane = new JScrollPane(paneInternals);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(300, 300));

    rawPane.add(scrollPane);

    messageTextField.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            RoomsState currentState = viewModel.getState();
            String currentMessage = currentState.getSendMessage();
            String message = "";
            if (currentMessage != null) {
              message = currentMessage;
            }
            currentState.setSendMessage(message + e.getKeyChar());
            viewModel.firePropertyChanged();
          }

          @Override
          public void keyPressed(KeyEvent e) {}

          @Override
          public void keyReleased(KeyEvent e) {}
        });

    send.addActionListener(
        evt -> {
          if (evt.getSource().equals(send)) {
            RoomsState currentState = viewModel.getState();
            String message = currentState.getSendMessage();
            String roomUid = currentState.getRoomUid();
            if (message != null) {
              User user = currentState.getUser();
              for (var room : currentState.getAvailableRooms()) {
                if (room.getUid().equals(roomUid)) {
                  roomsController.sendMessage(room, user, message);
                  break;
                }
              }
            }
          }
        });

    changeRoomButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(changeRoomButton)) {
            RoomsState currentState = viewModel.getState();
            // This needs to stay like this for now.
            // Changing rooms will be local to this file
            // and not involve the controller since we
            // already have everything here.
            currentState.setRoomUid("baz");
            viewModel.firePropertyChanged();
          }
        });

    refreshButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(refreshButton)) {
            RoomsState currentState = viewModel.getState();
            String roomUid = currentState.getRoomUid();
            User user = currentState.getUser();

            for (var room : currentState.getAvailableRooms()) {
              if (room.getUid().equals(roomUid)) {
                roomsController.loadMessages(room, user);
                break;
              }
            }
          }
        });
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    RoomsState state = (RoomsState) evt.getNewValue();
    String currentRoomUid = state.getRoomUid();

    String roomName = "";
    for (var room : state.getAvailableRooms()) {
      if (room.getUid().equals(currentRoomUid)) {
        roomName = room.getName();

        List<Message> messages = room.getMessages();
        state.setDisplayMessages(messages);

        paneInternals.removeAll();
        paneInternals.revalidate();
        paneInternals.repaint();

        for (var message : messages) {
          var messageView = new MessageView(message.content);
          paneInternals.add(messageView);
        }

        SwingUtilities.invokeLater(
            () -> {
              paneInternals.revalidate();
              paneInternals.repaint();
            });
      }
    }
    roomNameLabel.setText(state.getRoomUid());
  }
}
