import static java.lang.Thread.sleep;

import app.SettingsUseCaseFactory;
import app.SwitchViewUseCaseFactory;
import data_access.FirebaseSettingsDataAccessObject;
import entities.auth.User;
import entities.auth.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import java.time.LocalDateTime;
import javax.swing.*;
import org.junit.Assert;
import org.junit.Test;
import use_case.settings.SettingsDataAccessInterface;
import view.SettingsView;

public class SettingsTest {
  @Test
  public void testSettingsLoad() {
    // Dummy test user
    LocalDateTime date = LocalDateTime.now();
    UserFactory userFactory = new UserFactory();
    User test_user = userFactory.create("123", "test@gmail.com", "Test", "1234", date);

    CardLayout cardLayout = new CardLayout();
    JPanel views = new JPanel(cardLayout);
    ViewManagerModel viewManagerModel = new ViewManagerModel();

    SettingsDataAccessInterface settingsUserDataAccessObject =
        new FirebaseSettingsDataAccessObject();
    SettingsViewModel settingsViewModel = new SettingsViewModel();
    SwitchViewController switchViewController = SwitchViewUseCaseFactory.create(viewManagerModel);

    SettingsView settingsView =
        SettingsUseCaseFactory.create(
            settingsViewModel, settingsUserDataAccessObject, switchViewController);
    views.add(settingsView.contentPane, settingsView.viewName);

    JFrame jf = new JFrame();
    jf.setContentPane(settingsView.contentPane);
    jf.pack();
    jf.setVisible(true);

    try {
      sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    Assert.assertTrue(jf != null);
  }
}
