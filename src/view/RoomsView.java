package view;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import interface_adapter.rooms.LoadRoomsController;
import interface_adapter.rooms.RoomsController;
import interface_adapter.rooms.RoomsState;
import interface_adapter.rooms.RoomsViewModel;
import interface_adapter.search.SearchController;
import interface_adapter.search.StartSearchController;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Instant;
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
  private JTextField createRoomTextField;
  private JButton createRoomButton;
  private JButton addUserButton;
  private JLabel roomNameLabel2;
  private JTextField emailTextField;
  private JButton searchButton;
  private final RoomsViewModel viewModel;

  public RoomsView(
      RoomsViewModel viewModel,
      RoomsController roomsController,
      LoadRoomsController loadRoomsController,
      SearchController searchController,
      StartSearchController startSearchController) {
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

    emailTextField.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            RoomsState currentState = viewModel.getState();
            String currentEmail = currentState.getUserToAddEmail();
            String email = "";
            if (currentEmail != null) {
              email = currentEmail;
            }
            currentState.setUserToAddEmail(email + e.getKeyChar());
            viewModel.firePropertyChanged();
          }

          @Override
          public void keyPressed(KeyEvent e) {}

          @Override
          public void keyReleased(KeyEvent e) {}
        });

    // This is to resolve a crazy frontend issue
    createRoomTextField.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            // Handle in keyPressed
          }

          @Override
          public void keyPressed(KeyEvent e) {
            RoomsState currentState = viewModel.getState();
            String currentRoomToCreateName = currentState.getRoomToCreateName();
            String roomToCreateName =
                currentRoomToCreateName != null ? currentRoomToCreateName : "";

            if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
              if (!roomToCreateName.isEmpty()) {
                roomToCreateName = roomToCreateName.substring(0, roomToCreateName.length() - 1);
              }
            } else {
              roomToCreateName += e.getKeyChar();
            }

            currentState.setRoomToCreateName(roomToCreateName);
            viewModel.firePropertyChanged();
          }

          @Override
          public void keyReleased(KeyEvent e) {}
        });

    send.addActionListener(
        evt -> {
          if (evt.getSource().equals(send)) {
            RoomsState currentState = viewModel.getState();
            String message = currentState.getSendMessage();
            if (message != null && currentState.roomIsSelected()) {
              Room room = currentState.getRoomByUid();
              User user = currentState.getUser();
              roomsController.sendMessage(room, user, message);
              searchController.executeRecordData(
                  Instant.now(), currentState.getRoomUid(), message, currentState.getUserUid());
            }
          }
        });

    refreshButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(refreshButton)) {
            RoomsState currentState = viewModel.getState();
            User user = currentState.getUser();

            loadRoomsController.loadRooms(user);
          }
        });

    addUserButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(addUserButton)) {
            RoomsState currentState = viewModel.getState();
            String email = currentState.getUserToAddEmail();
            String roomUid = currentState.getRoomUid();
            if (email != null) {
              User user = currentState.getUser();
              for (var room : currentState.getAvailableRooms()) {
                if (room.getUid().equals(roomUid)) {
                  roomsController.addUserToRoom(room, user, email);
                  break;
                }
              }
            }
          }
        });

    createRoomButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(createRoomButton)) {
            RoomsState currentState = viewModel.getState();
            String roomToCreateName = currentState.getRoomToCreateName();
            if (roomToCreateName != null) {
              User user = currentState.getUser();

              roomsController.createRoom(user, roomToCreateName);
            }
          }
        });

    searchButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(searchButton)) {
            RoomsState currentState = viewModel.getState();
            String uid = currentState.getUserUid();
            String roomUid = currentState.getRoomUid();
            startSearchController.search(roomUid, uid);
          }
        });
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    RoomsState state = (RoomsState) evt.getNewValue();
    String currentRoomUid = state.getRoomUid();
    String name = "";

    List<Room> rooms = state.getAvailableRooms();
    roomsPaneInternals.removeAll();
    roomsPaneInternals.revalidate();
    roomsPaneInternals.repaint();

    for (var r : rooms) {
      var roomView = new ListedRoom(r, viewModel);
      roomsPaneInternals.add(roomView);

      if (r.getUid().equals(currentRoomUid)) {
        name += r.getName();
        roomNameLabel.removeAll();
        roomNameLabel.revalidate();
        roomNameLabel.repaint();
        roomNameLabel.setText(name);

        List<Message> messages = r.getMessages();
        state.setDisplayMessages(messages);

        messagesPaneInternals.removeAll();
        messagesPaneInternals.revalidate();
        messagesPaneInternals.repaint();

        for (var message : messages) {
          var messageView = new MessageView(message.content, message.displayUser.getName());
          messagesPaneInternals.add(messageView);
        }

        // See https://horstmann.com/unblog/2007-06-11/swing-single-thread-rule.html
        SwingUtilities.invokeLater(
            () -> {
              messagesPaneInternals.revalidate();
              messagesPaneInternals.repaint();
              JScrollPane scrollPane = (JScrollPane) messagesPane.getComponent(0);
              JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
              verticalScrollBar.setValue(verticalScrollBar.getMaximum());
            });
      }
    }

    if (name.equals("")) {
      roomNameLabel2.setText("Select a room");
    } else {
      roomNameLabel2.setText("Add friend to " + name);
    }

    if (state.getError() != null) {
      JOptionPane.showMessageDialog(contentPane, state.getError());
      state.setError(null);
    }
    if (state.getSuccess() != null) {
      JOptionPane.showMessageDialog(contentPane, state.getSuccess());
      state.setSuccess(null);
    }
  }
}
