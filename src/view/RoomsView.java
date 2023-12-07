package view;

import entities.rooms.Message;
import entities.rooms.Room;
import interface_adapter.room_settings.OpenRoomSettingsController;
import interface_adapter.rooms.LoadRoomsController;
import interface_adapter.rooms.RoomsController;
import interface_adapter.rooms.RoomsState;
import interface_adapter.rooms.RoomsViewModel;
import interface_adapter.search.SearchController;
import interface_adapter.search.StartSearchController;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Instant;
import java.util.List;
import javax.swing.*;

public class RoomsView implements PropertyChangeListener {
  public static final int INTERVAL = 500;
  public JPanel contentPane;
  private final JPanel messagesPaneInternals;
  private final JPanel roomsPaneInternals;
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
  private JButton roomSettingsButton;
  private final RoomsViewModel viewModel;

  public RoomsView(
      RoomsViewModel viewModel,
      RoomsController roomsController,
      LoadRoomsController loadRoomsController,
      SearchController searchController,
      StartSearchController startSearchController,
      OpenRoomSettingsController openRoomSettingsController) {
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

    messageTextField
        .getDocument()
        .addDocumentListener(
            new DocumentUpdateListener() {
              public void update() {
                RoomsState currentState = viewModel.getState();
                currentState.setSendMessage(messageTextField.getText());
                viewModel.firePropertyChanged();
              }
            });

    emailTextField
        .getDocument()
        .addDocumentListener(
            new DocumentUpdateListener() {
              public void update() {
                RoomsState currentState = viewModel.getState();
                currentState.setUserToAddEmail(emailTextField.getText());
                viewModel.firePropertyChanged();
              }
            });

    // This is to resolve a crazy frontend issue
    createRoomTextField
        .getDocument()
        .addDocumentListener(
            new DocumentUpdateListener() {

              public void update() {
                RoomsState currentState = viewModel.getState();
                String roomToCreateName = createRoomTextField.getText();
                currentState.setRoomToCreateName(roomToCreateName);
                viewModel.firePropertyChanged();
              }
            });

    send.addActionListener(
        evt -> {
          if (evt.getSource().equals(send)) {
            RoomsState currentState = viewModel.getState();
            String message = currentState.getSendMessage();
            if (message != null && currentState.roomIsSelected()) {
              Room room = currentState.getRoomByUid();
              roomsController.sendMessage(room, message);
              messageTextField.setText("");
              searchController.executeRecordData(Instant.now(), currentState.getRoomUid(), message);
            }
          }
        });

    refreshButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(refreshButton)) {
            loadRoomsController.loadRooms();
          }
        });

    roomSettingsButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(roomSettingsButton)) {
            RoomsState currentState = viewModel.getState();
            if (currentState.roomIsSelected()) {
              Room room = currentState.getRoomByUid();
              openRoomSettingsController.open(room);
            }
          }
        });

    Timer timer = new Timer(INTERVAL, evt -> loadRoomsController.loadRooms());
    timer.start();

    addUserButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(addUserButton)) {
            RoomsState currentState = viewModel.getState();
            String email = currentState.getUserToAddEmail();
            String roomUid = currentState.getRoomUid();
            if (email != null) {
              for (var room : currentState.getAvailableRooms()) {
                if (room.getUid().equals(roomUid)) {
                  roomsController.addUserToRoom(room, email);
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
              roomsController.createRoom(roomToCreateName);
            }
          }
        });

    searchButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(searchButton)) {
            RoomsState currentState = viewModel.getState();
            String roomUid = currentState.getRoomUid();
            startSearchController.search(roomUid);
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
          var messageView =
              new MessageView(message.getContent(), message.getDisplayUser().getName());
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

    if (name.isEmpty()) {
      roomNameLabel2.setText("Select a room");
    } else {
      roomNameLabel2.setText("Add friend to " + name);
    }

    if (state.getError() != null) {
      JOptionPane.showMessageDialog(contentPane, state.getError());
    }
    if (state.getSuccess() != null) {
      JOptionPane.showMessageDialog(contentPane, state.getSuccess());
      state.setSuccess(null);
    }
  }

  // For testing
  public JButton getRefreshButton() {
    return refreshButton;
  }

  public JButton getCreateRoomButton() {
    return createRoomButton;
  }

  public JButton getAddUserButton() {
    return addUserButton;
  }

  public JButton getSendMessageButton() {
    return send;
  }

  public JButton getSearchButton() {
    return searchButton;
  }
}
