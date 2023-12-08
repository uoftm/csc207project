package view;

import entities.rooms.Room;
import interface_adapter.rooms.RoomsState;
import interface_adapter.rooms.RoomsViewModel;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

/**
 * The ListedRoom class represents a graphical panel that displays information about a room. It
 * extends JPanel and provides a clickable label to select the room.
 */
public class ListedRoom extends JPanel {
  /**
   * Initializes a new instance of the ListedRoom class.
   *
   * @param room The room to display information about.
   * @param viewModel The view model to update when the room is selected.
   */
  public ListedRoom(Room room, RoomsViewModel viewModel) {
    String roomName = room.getName(); // Cannot be null
    JLabel roomNameLabel = new JLabel(roomName);
    roomNameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand on hover
    roomNameLabel.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            String roomUid = room.getUid(); // Cannot be null
            RoomsState currentState = viewModel.getState();
            currentState.setRoomUid(roomUid);
            viewModel.firePropertyChanged();
          }
        });
    this.add(roomNameLabel);
  }
}
