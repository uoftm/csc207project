package view;

import interface_adapter.settings.SettingsController;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SettingsView implements PropertyChangeListener {

  public static final String viewName = "settings";
  private final SettingsViewModel settingsViewModel;
  private final SettingsController settingsController;
  public JPanel contentPane;
  private JPanel body;
  private JLabel title;
  private JButton backButton;
  private JTextField changeUsernameField;
  private JButton changeUsernameButton;

  public SettingsView(
      SettingsViewModel settingsViewModel,
      SettingsController controller,
      SwitchViewController switchViewController) {
    this.settingsController = controller;
    this.settingsViewModel = settingsViewModel;
    this.settingsViewModel.addPropertyChangeListener(this);

    contentPane.setBackground(ViewConstants.background);
    contentPane.setPreferredSize(ViewConstants.windowSize);
    body.setBackground(ViewConstants.panel);
    body.setPreferredSize(ViewConstants.paneSize);

    backButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(backButton)) {
            switchViewController.switchTo(LoggedInView.viewName);
          }
        });

    changeUsernameButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(changeUsernameButton)) {
            settingsController.executeChangeUsername(
                settingsViewModel.getState().getUpdatedUsername(),
                settingsViewModel.getState().getUser());
          }
        });

    changeUsernameField
        .getDocument()
        .addDocumentListener(
            new DocumentListener() {
              @Override
              public void insertUpdate(DocumentEvent e) {
                update();
              }

              @Override
              public void removeUpdate(DocumentEvent e) {
                update();
              }

              @Override
              public void changedUpdate(DocumentEvent e) {
                update();
              }

              protected void update() {
                String text = changeUsernameField.getText();
                SettingsState currentstate = settingsViewModel.getState();
                currentstate.setUpdatedUsername(text);
                settingsViewModel.setState(currentstate);
              }
            });
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    SettingsState state = (SettingsState) evt.getNewValue();
    if (state.getIsError() || state.getIsSuccess()) {
      JOptionPane.showMessageDialog(contentPane, state.getMessage());
    }
  }
}
