package view;

import static java.lang.Thread.sleep;

import app.SwitchViewUseCaseFactory;
import data_access.DAOTest;
import data_access.InMemoryUserDataAccessObject;
import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Room;
import interface_adapter.ViewManagerModel;
import interface_adapter.room_settings.RoomSettingsController;
import interface_adapter.room_settings.RoomSettingsPresenter;
import interface_adapter.room_settings.RoomSettingsViewModel;
import interface_adapter.rooms.RoomsViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import java.util.List;
import javax.swing.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import use_case.login.LoginUserDataAccessInterface;
import use_case.room_settings.RoomSettingsInteractor;
import use_case.rooms.LoggedInDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;

public class RoomSettingsTest {
  RoomSettingsViewModel roomSettingsViewModel;
  RoomSettingsPresenter roomSettingsPresenter;
  RoomsViewModel roomsViewModel;
  ViewManagerModel viewManagerModel;
  SwitchViewController switchViewController;
  LoginUserDataAccessInterface userDataAccessObject;
  LoggedInDataAccessInterface inMemoryDAO;
  JPanel views;

  @Before
  public void setUp() {
    CardLayout cardLayout = new CardLayout();
    views = new JPanel(cardLayout);
    viewManagerModel = new ViewManagerModel();

    switchViewController = SwitchViewUseCaseFactory.create(viewManagerModel);
    userDataAccessObject =
        new LoginUserDataAccessInterface() {
          @Override
          public User getUser(String idToken, String email, String password) {
            return null;
          }

          @Override
          public String getAccessToken(String email, String password) {
            return null;
          }

          @Override
          public DisplayUser getDisplayUser(String email) {
            return null;
          }
        };

    roomSettingsViewModel = new RoomSettingsViewModel();
    roomsViewModel = new RoomsViewModel();
    inMemoryDAO = new InMemoryUserDataAccessObject();
    inMemoryDAO.setIdToken("dummy token");
    roomSettingsPresenter =
        new RoomSettingsPresenter(roomsViewModel, roomSettingsViewModel, viewManagerModel);
  }

  @Test
  public void testRoomSettingsLoad() throws InterruptedException {
    var roomsDataAccessObject =
        new RoomsDataAccessInterface() {

          public int deleteRoomCalls = 0;
          public int changeRoomNameCalls = 0;

          @Override
          public Room getRoomFromId(String idToken, User user, String roomId) {
            return null;
          }

          @Override
          public Room addRoom(String idToken, User user, String roomName) {
            return null;
          }

          @Override
          public void deleteRoom(String idToken, User user, Room room) {
            deleteRoomCalls++;
          }

          @Override
          public void addUserToRoom(
              String idToken, User currentUser, DisplayUser newUser, Room room) {}

          @Override
          public List<String> getAvailableRoomIds(User user) {
            return null;
          }

          @Override
          public void removeUserFromRoom(
              String idToken, User currentUser, DisplayUser userToRemove, Room room) {}

          @Override
          public void changeRoomName(String idToken, User user, Room activeRoom, String roomName) {
            changeRoomNameCalls++;
            Assert.assertEquals("Test Room 2", roomName);
          }
        };
    RoomSettingsInteractor roomSettingsInteractor =
        new RoomSettingsInteractor(roomsDataAccessObject, inMemoryDAO, roomSettingsPresenter);

    RoomSettingsController roomSettingsController =
        new RoomSettingsController(roomSettingsInteractor);
    RoomSettingsView roomSettingsView =
        new RoomSettingsView(roomSettingsViewModel, roomSettingsController, switchViewController);
    views.add(roomSettingsView.contentPane, roomSettingsView.viewName);

    JFrame jf = new JFrame();
    jf.setContentPane(roomSettingsView.contentPane);
    jf.pack();
    jf.setVisible(true);
    Room activeRoom = DAOTest.createDummyRoom();
    roomSettingsViewModel.setActiveRoom(activeRoom);
    inMemoryDAO.setUser(DAOTest.createDummyUser());

    var roomsState = roomsViewModel.getState();
    var availableRooms = roomsState.getAvailableRooms();
    availableRooms.add(activeRoom);
    roomsViewModel.setState(roomsState);
    roomsViewModel.firePropertyChanged();

    sleep(1000);

    roomSettingsView.getRoomName().setText("Test Room 2");
    sleep(100);
    roomSettingsView.getSaveButton().doClick();
    sleep(100);
    Assert.assertEquals(1, roomsDataAccessObject.changeRoomNameCalls);
    Assert.assertEquals("Test Room 2", activeRoom.getName());

    // The user of the tests must click no then yes to pass this test
    roomSettingsView.getDeleteRoomButton().doClick();
    sleep(100);
    Assert.assertEquals(0, roomsDataAccessObject.deleteRoomCalls);
    Assert.assertEquals(1, availableRooms.size());

    roomSettingsView.getDeleteRoomButton().doClick();
    sleep(100);
    Assert.assertEquals(1, roomsDataAccessObject.deleteRoomCalls);
    Assert.assertEquals(0, availableRooms.size());

    roomSettingsView.getBackButton().doClick();
    sleep(100);
    Assert.assertEquals(LoggedInView.viewName, viewManagerModel.getActiveView());
  }

  @Test
  public void testRoomSettingsFailures() throws InterruptedException {
    var roomsDataAccessObject =
        new RoomsDataAccessInterface() {

          @Override
          public Room getRoomFromId(String idToken, User user, String roomId) {
            return null;
          }

          @Override
          public Room addRoom(String idToken, User user, String roomName) {
            return null;
          }

          @Override
          public void deleteRoom(String idToken, User user, Room room) {
            throw new RuntimeException("Failed to delete room.");
          }

          @Override
          public void addUserToRoom(
              String idToken, User currentUser, DisplayUser newUser, Room room) {}

          @Override
          public List<String> getAvailableRoomIds(User user) {
            return null;
          }

          @Override
          public void removeUserFromRoom(
              String idToken, User currentUser, DisplayUser userToRemove, Room room) {}

          @Override
          public void changeRoomName(String idToken, User user, Room activeRoom, String roomName) {
            throw new RuntimeException("Failed to change room name.");
          }
        };

    RoomSettingsInteractor roomSettingsInteractor =
        new RoomSettingsInteractor(roomsDataAccessObject, inMemoryDAO, roomSettingsPresenter);

    RoomSettingsController roomSettingsController =
        new RoomSettingsController(roomSettingsInteractor);
    RoomSettingsView roomSettingsView =
        new RoomSettingsView(roomSettingsViewModel, roomSettingsController, switchViewController);
    views.add(roomSettingsView.contentPane, roomSettingsView.viewName);

    JFrame jf = new JFrame();
    jf.setContentPane(roomSettingsView.contentPane);
    jf.pack();
    jf.setVisible(true);

    Room activeRoom = DAOTest.createDummyRoom();
    roomSettingsViewModel.setActiveRoom(activeRoom);
    inMemoryDAO.setUser(DAOTest.createDummyUser());

    var roomsState = roomsViewModel.getState();
    var availableRooms = roomsState.getAvailableRooms();
    availableRooms.add(activeRoom);
    roomsViewModel.setState(roomsState);
    roomsViewModel.firePropertyChanged();

    sleep(1000);

    roomSettingsView.getRoomName().setText("Test Room 2");
    sleep(100);
    roomSettingsView.getSaveButton().doClick();
    sleep(100);
    Assert.assertEquals("Failed to change room name.", roomSettingsViewModel.getError());

    roomSettingsView.getDeleteRoomButton().doClick();
    sleep(100);
    Assert.assertEquals("Failed to delete room.", roomSettingsViewModel.getError());

    roomSettingsView.getBackButton().doClick();
    sleep(100);
    Assert.assertEquals(LoggedInView.viewName, viewManagerModel.getActiveView());
  }
}
