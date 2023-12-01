package view;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
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
  private JPanel messagesPaneInternals;
  private JPanel roomsPaneInternals;
  private JTextField messageTextField;
  private JButton send;
  private JPanel messagesPane;
  private JLabel roomNameLabel;
  private JButton refreshButton;
  private JPanel roomsPane;
  private final RoomsViewModel viewModel;

  public RoomsView(RoomsViewModel viewModel, RoomsController roomsController) {
    this.viewModel = viewModel;
    this.viewModel.addPropertyChangeListener(this);

    messagesPane.setLayout(new BoxLayout(messagesPane, BoxLayout.Y_AXIS));
    roomsPane.setLayout(new BoxLayout(roomsPane, BoxLayout.Y_AXIS));

    messagesPaneInternals = new JPanel();
    messagesPaneInternals.setLayout(new BoxLayout(messagesPaneInternals, BoxLayout.Y_AXIS));

    JScrollPane messagesScrollPane = new JScrollPane(messagesPaneInternals);
    messagesScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    messagesScrollPane.setPreferredSize(new Dimension(300, 300));

    messagesPane.add(messagesScrollPane);

    roomsPaneInternals = new JPanel();
    roomsPaneInternals.setLayout(new BoxLayout(roomsPaneInternals, BoxLayout.Y_AXIS));

    JScrollPane roomsScrollPane = new JScrollPane(roomsPaneInternals);
    roomsScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    roomsScrollPane.setPreferredSize(new Dimension(300, 300));

    roomsPane.add(roomsScrollPane);

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

    List<Room> rooms = state.getAvailableRooms();
    roomsPaneInternals.removeAll();
    roomsPaneInternals.revalidate();
    roomsPaneInternals.repaint();

    for (var room : state.getAvailableRooms()) {
      var roomView = new ListedRoom(room, viewModel);
      roomsPaneInternals.add(roomView);

      if (room.getUid().equals(currentRoomUid)) {
        roomName = room.getName();

        List<Message> messages = room.getMessages();
        state.setDisplayMessages(messages);

        messagesPaneInternals.removeAll();
        messagesPaneInternals.revalidate();
        messagesPaneInternals.repaint();

        for (var message : messages) {
          var messageView = new MessageView(message.content);
          messagesPaneInternals.add(messageView);
        }

        // See https://horstmann.com/unblog/2007-06-11/swing-single-thread-rule.html
        SwingUtilities.invokeLater(
            () -> {
              messagesPaneInternals.revalidate();
              messagesPaneInternals.repaint();
            });
      }
    }

    roomNameLabel.setText(roomName);
  }
}
