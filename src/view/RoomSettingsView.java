package view;

import entities.rooms.Room;
import interface_adapter.room_settings.RoomSettingsController;
import interface_adapter.room_settings.RoomSettingsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class RoomSettingsView implements PropertyChangeListener {
  public static final String viewName = "room-settings";

  public JPanel contentPane;
  private JTextField roomName;
  private JButton backButton;
  private JButton saveButton;
  private JPanel rawPanel;
  private JButton deleteRoomButton;

  public RoomSettingsView(
      RoomSettingsViewModel viewModel,
      RoomSettingsController roomSettingsController,
      SwitchViewController switchViewController) {
    contentPane.setBackground(ViewConstants.background);
    contentPane.setPreferredSize(ViewConstants.windowSize);

    viewModel.addPropertyChangeListener(this);

    backButton.addActionListener(
        e -> {
          if (e.getSource().equals(backButton)) {
            switchViewController.switchTo(LoggedInView.viewName);
          }
        });

    saveButton.addActionListener(
        e -> {
          if (e.getSource().equals(saveButton)) {
            roomSettingsController.saveRoomName(
                viewModel.getUser(), viewModel.getActiveRoom(), roomName.getText());
          }
        });

    deleteRoomButton.addActionListener(
        e -> {
          if (e.getSource().equals(deleteRoomButton)) {
            var confirm =
                JOptionPane.showConfirmDialog(
                    contentPane,
                    "Are you sure you want to delete this room?",
                    "Delete Room",
                    JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
              roomSettingsController.deleteRoom(viewModel.getUser(), viewModel.getActiveRoom());
            }
          }
        });
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    if (evt.getPropertyName().equals("activeRoom")) {
      var room = (Room) evt.getNewValue();
      roomName.setText(room.getName());
    }
    if (evt.getPropertyName().equals("error")) {
      JOptionPane.showMessageDialog(contentPane, evt.getNewValue());
    }
  }

  public JButton getBackButton() {
    return backButton;
  }

  public JButton getSaveButton() {
    return saveButton;
  }

  public JTextField getRoomName() {
    return roomName;
  }

  public JButton getDeleteRoomButton() {
    return deleteRoomButton;
  }
}
