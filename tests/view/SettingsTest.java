package view;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

import app.SettingsUseCaseFactory;
import app.SwitchViewUseCaseFactory;
import data_access.FirebaseRoomsDataAccessObject;
import data_access.FirebaseUserDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import entities.auth.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.settings.SettingsState;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import javax.swing.*;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import use_case.rooms.LoggedInDataAccessInterface;
import use_case.settings.RoomsSettingsDataAccessInterface;

public class SettingsTest {
  private final OkHttpClient client = new OkHttpClient();

  JPanel views;
  ViewManagerModel viewManagerModel;

  FirebaseUserDataAccessObject userDao;

  RoomsSettingsDataAccessInterface roomsSettingsDataAccessObject;

  SettingsViewModel settingsViewModel;

  SwitchViewController switchViewController;

  LoggedInDataAccessInterface inMemoryDAO;

  @Before
  public void setUp() {
    CardLayout cardLayout = new CardLayout();
    views = new JPanel(cardLayout);
    viewManagerModel = new ViewManagerModel();
    userDao = new FirebaseUserDataAccessObject(client);
    roomsSettingsDataAccessObject = new FirebaseRoomsDataAccessObject(client);
    settingsViewModel = new SettingsViewModel();
    switchViewController = SwitchViewUseCaseFactory.create(viewManagerModel);
    inMemoryDAO = new InMemoryUserDataAccessObject();
  }


  @Test
  public void testSettingsLoad() {
    SettingsView settingsView =
        SettingsUseCaseFactory.create(
            settingsViewModel,
            null,
            userDao,
            roomsSettingsDataAccessObject,
            inMemoryDAO,
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

    Assert.assertNotNull(jf);
  }

  @Test
  public void testSettingsChangeUsername() {
    User user = userDao.getUser(inMemoryDAO.getIdToken(), "abc@gmail.com", "1234567");
    LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
    SettingsView settingsView =
        SettingsUseCaseFactory.create(
            settingsViewModel,
            loggedInViewModel,
            userDao,
            roomsSettingsDataAccessObject,
            inMemoryDAO,
            switchViewController);
    views.add(settingsView.contentPane, settingsView.viewName);
    settingsView.getChangeUsernameField().setText("TestName");
    SettingsState currentstate = settingsViewModel.getState();
    currentstate.setUpdatedUsername(user.getName());
    settingsView.getChangeUsernameButton().doClick();
    SettingsState settingsState = settingsViewModel.getState();
    assertEquals("Successfully updated username", settingsState.getMessage());
  }

  @Test
  public void testSettingsChangeUsernameFail() {
    var roomsDataAccessObject =
        new RoomsSettingsDataAccessInterface() {
          public void propogateDisplayNameChange(String tokenId, User user) {
            throw new RuntimeException("Failed to update username");
          }
        };

    User user = userDao.getUser(inMemoryDAO.getIdToken(), "abc@gmail.com", "1234567");
    LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
    SettingsView settingsView =
        SettingsUseCaseFactory.create(
            settingsViewModel,
            loggedInViewModel,
            userDao,
            roomsDataAccessObject,
            inMemoryDAO,
            switchViewController);
    views.add(settingsView.contentPane, settingsView.viewName);
    settingsView.getChangeUsernameField().setText("TestName");
    SettingsState currentstate = settingsViewModel.getState();
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
            inMemoryDAO,
            switchViewController);
    views.add(settingsView.contentPane, settingsView.viewName);
    settingsView.getBackButton().doClick();
    assertEquals("logged in", viewManagerModel.getActiveView());
  }
}
