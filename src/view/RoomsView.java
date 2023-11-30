package view;

import interface_adapter.rooms.RoomsController;
import interface_adapter.rooms.RoomsViewModel;
import interface_adapter.settings.SettingsState;
import interface_adapter.switch_view.SwitchViewController;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class RoomsView implements PropertyChangeListener {
    public static final String viewName = "rooms";
    private final RoomsViewModel roomsViewModel;
    private final RoomsController roomsController;
    private JPanel panel1;
    public JPanel contentPane;

    public RoomsView(
            RoomsViewModel roomViewModel,
            RoomsController controller,
            SwitchViewController switchViewController) {
        this.roomsController = controller;
        this.roomsViewModel = roomViewModel;
        this.roomsViewModel.addPropertyChangeListener(this);

        contentPane.setBackground(ViewConstants.background);
        contentPane.setPreferredSize(ViewConstants.windowSize);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        SettingsState state = (SettingsState) evt.getNewValue();
        if (state.getError() != null) {
            JOptionPane.showMessageDialog(contentPane, state.getError());
        }
    }
}
