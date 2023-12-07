package view;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

import app.SettingsUseCaseFactory;
import app.SwitchViewUseCaseFactory;
import data_access.FirebaseRoomsDataAccessObject;
import data_access.FirebaseUserDataAccessObject;
import entities.auth.User;
import entities.auth.UserFactory;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.swing.*;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import use_case.login.LoginUserDataAccessInterface;
import use_case.settings.RoomsSettingsDataAccessInterface;

public class SettingsTest {

  private final OkHttpClient client = new OkHttpClient();

  JPanel views;
  ViewManagerModel viewManagerModel;

  FirebaseUserDataAccessObject userDao;

  RoomsSettingsDataAccessInterface roomsSettingsDataAccessObject;

  SettingsViewModel settingsViewModel;

  SwitchViewController switchViewController;

  @Before
  public void setUp() {
    LocalDateTime date = LocalDateTime.now();
    UserFactory userFactory = new UserFactory();
    User test_user = userFactory.create("123", "test@gmail.com", "Test", "1234", date);
    CardLayout cardLayout = new CardLayout();
    views = new JPanel(cardLayout);
    viewManagerModel = new ViewManagerModel();
    userDao = new FirebaseUserDataAccessObject(client);
    roomsSettingsDataAccessObject = new FirebaseRoomsDataAccessObject(client);
    settingsViewModel = new SettingsViewModel();
    switchViewController = SwitchViewUseCaseFactory.create(viewManagerModel);
  }

  User createDummyUser() {
    String fakeEmail =
        String.format("testSaveUser%s@example.com", UUID.randomUUID().toString().substring(0, 10));
    return new User(null, fakeEmail, "Dummy User", "password", LocalDateTime.now());
  }

  @Test
  public void testSettingsLoad() {
    SettingsView settingsView =
        SettingsUseCaseFactory.create(
            settingsViewModel,
            null,
            userDao,
            roomsSettingsDataAccessObject,
            userDao,
            switchViewController);
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

  @Test
  public void testSettingsChangeUsername() {
    User user = userDao.getUser("abc@gmail.com", "1234567");
    LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
    SettingsView settingsView =
        SettingsUseCaseFactory.create(
            settingsViewModel,
            loggedInViewModel,
            userDao,
            roomsSettingsDataAccessObject,
            userDao,
            switchViewController);
    views.add(settingsView.contentPane, settingsView.viewName);
    settingsView.getChangeUsernameField().setText("TestName");
    SettingsState currentstate = settingsViewModel.getState();
    currentstate.setUser(user);
    currentstate.setUpdatedUsername(user.getName());
    settingsView.getChangeUsernameButton().doClick();
    SettingsState settingsState = settingsViewModel.getState();
    assertEquals("Successfully updated username", settingsState.getMessage());
  }

  @Test
  public void testSettingsChangeUsernameFail() {
    var roomsDataAccessObject =
        new RoomsSettingsDataAccessInterface() {
          public void propogateDisplayNameChange(User user, LoginUserDataAccessInterface userDao) {
            throw new RuntimeException("Failed to update username");
          }
        };
    User user = userDao.getUser("abc@gmail.com", "1234567");
    LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
    SettingsView settingsView =
        SettingsUseCaseFactory.create(
            settingsViewModel,
            loggedInViewModel,
            userDao,
            roomsDataAccessObject,
            userDao,
            switchViewController);
    views.add(settingsView.contentPane, settingsView.viewName);
    settingsView.getChangeUsernameField().setText("TestName");
    SettingsState currentstate = settingsViewModel.getState();
    currentstate.setUser(user);
    currentstate.setUpdatedUsername(user.getName());
    settingsView.getChangeUsernameButton().doClick();
    SettingsState settingsState = settingsViewModel.getState();
    assertEquals("Failed to update username", settingsState.getMessage());
  }

  @Test
  public void testClickBackButton() {
    SettingsView settingsView =
        SettingsUseCaseFactory.create(
            settingsViewModel,
            null,
            userDao,
            roomsSettingsDataAccessObject,
            userDao,
            switchViewController);
    views.add(settingsView.contentPane, settingsView.viewName);
    settingsView.getBackButton().doClick();
    assertEquals("logged in", viewManagerModel.getActiveView());
  }
}
