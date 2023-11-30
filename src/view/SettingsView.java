package view;

import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

public class SettingsView implements PropertyChangeListener {

    public static final String viewName = "settings";
    private final SettingsViewModel settingsViewModel;

    private final SettingsController settingsController;
    public JPanel contentPane;

    public SettingsView(
            SettingsViewModel settingsViewModel,
            SettingsController controller,
            SwitchViewController switchViewController) {
        this.settingsController = controller;
        this.settingsViewModel = settingsViewModel;
        this.settingsViewModel.addPropertyChangeListener(this);

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
