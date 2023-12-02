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
  private JTextField createRoomTextField;
  private JButton createRoomButton;
  private JButton addUserButton;
  private JLabel roomNameLabel2;
  private JTextField emailTextField;
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

    emailTextField.addKeyListener(
            new KeyListener() {
              @Override
              public void keyTyped(KeyEvent e) {
                RoomsState currentState = viewModel.getState();
                String currentEmail = currentState.getEmail();
                String email = "";
                if (currentEmail != null) {
                  email = currentEmail;
                }
                currentState.setEmail(email + e.getKeyChar());
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
                      String currentCreateRoom = currentState.getCreateRoom();
                      String createRoom = currentCreateRoom != null ? currentCreateRoom : "";

                      if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE || e.getKeyCode() == KeyEvent.VK_DELETE) {
                          if (!createRoom.isEmpty()) {
                              createRoom = createRoom.substring(0, createRoom.length() - 1);
                          }
                      } else {
                          createRoom += e.getKeyChar();
                      }

                      currentState.setCreateRoom(createRoom);
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
            User user = currentState.getUser();
            String roomUid = currentState.getRoomUid();

            for (var room : currentState.getAvailableRooms()) {
              if (room.getUid().equals(roomUid)) {
                roomsController.loadMessages(room, user);
              }
            }
          }
        });

    addUserButton.addActionListener(
            evt -> {
              if (evt.getSource().equals(addUserButton)) {
                RoomsState currentState = viewModel.getState();
                String email = currentState.getEmail();
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
                String createRoom = currentState.getCreateRoom();
                if (createRoom != null) {
                  User user = currentState.getUser();
                  roomsController.createRoom(user, createRoom);
                }
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
          // String name = room.getUid();
          name += r.getName();
          System.out.println(name);
          roomNameLabel.removeAll();
          roomNameLabel.revalidate();
          roomNameLabel.repaint();
          System.out.println(roomNameLabel.getText());
            roomNameLabel.setText(name);

        List<Message> messages = r.getMessages();
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

    if (name.equals("")) {
      roomNameLabel2.setText("Select a room");
    } else {
      roomNameLabel2.setText("Add friend to " + name);
    }

    if (state.getError() != null) {
      JOptionPane.showMessageDialog(contentPane, state.getError());
    }
    if (state.getSuccess() != null) {
        JOptionPane.showMessageDialog(contentPane, state.getSuccess());
    }
  }
}
