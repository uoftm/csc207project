package view;

import entities.rooms.Room;
import interface_adapter.rooms.RoomsState;
import interface_adapter.rooms.RoomsViewModel;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class RoomView extends JPanel {
  public RoomView(Room room, RoomsViewModel viewModel) {
    String roomName = room.getName(); // Cannot be null (there should be a test for this)
    JLabel roomNameLabel = new JLabel(roomName);
    roomNameLabel.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Change cursor to hand on hover
    roomNameLabel.addMouseListener(
        new MouseAdapter() {
          @Override
          public void mouseClicked(MouseEvent e) {
            String roomUid = room.getUid(); // Cannot be null (there should be a test for this)
            RoomsState currentState = viewModel.getState();
            currentState.setRoomUid(roomUid);
            viewModel.firePropertyChanged();
          }
        });
    this.add(roomNameLabel);
  }
}
