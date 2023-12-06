package view;

import static java.lang.Thread.sleep;

import app.SwitchViewUseCaseFactory;
import data_access.DAOTest;
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
import use_case.rooms.RoomsDataAccessInterface;

public class RoomSettingsTest {
  RoomSettingsViewModel roomSettingsViewModel;
  RoomSettingsPresenter roomSettingsPresenter;
  RoomsViewModel roomsViewModel;
  ViewManagerModel viewManagerModel;
  SwitchViewController switchViewController;
  LoginUserDataAccessInterface userDataAccessObject;
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
          public User getUser(String email, String password) {
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
          public Room getRoomFromId(
              User user, LoginUserDataAccessInterface userDAO, String roomId) {
            return null;
          }

          @Override
          public Room addRoom(User user, LoginUserDataAccessInterface userDAO, String roomName) {
            return null;
          }

          @Override
          public void deleteRoom(User user, LoginUserDataAccessInterface userDAO, Room room) {
            deleteRoomCalls++;
          }

          @Override
          public void addUserToRoom(
              User currentUser,
              DisplayUser newUser,
              LoginUserDataAccessInterface userDAO,
              Room room) {}

          @Override
          public List<String> getAvailableRoomIds(User user) {
            return null;
          }

          @Override
          public void removeUserFromRoom(
              User currentUser,
              DisplayUser userToRemove,
              LoginUserDataAccessInterface userDAO,
              Room room) {}

          @Override
          public void changeRoomName(
              User user, LoginUserDataAccessInterface userDAO, Room activeRoom, String roomName) {
            changeRoomNameCalls++;
            Assert.assertEquals("Test Room 2", roomName);
          }
        };
    RoomSettingsInteractor roomSettingsInteractor =
        new RoomSettingsInteractor(
            roomsDataAccessObject, userDataAccessObject, roomSettingsPresenter);

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
    roomSettingsViewModel.setUser(DAOTest.createDummyUser());

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
          public Room getRoomFromId(
              User user, LoginUserDataAccessInterface userDAO, String roomId) {
            return null;
          }

          @Override
          public Room addRoom(User user, LoginUserDataAccessInterface userDAO, String roomName) {
            return null;
          }

          @Override
          public void deleteRoom(User user, LoginUserDataAccessInterface userDAO, Room room) {
            throw new RuntimeException("Failed to delete room.");
          }

          @Override
          public void addUserToRoom(
              User currentUser,
              DisplayUser newUser,
              LoginUserDataAccessInterface userDAO,
              Room room) {}

          @Override
          public List<String> getAvailableRoomIds(User user) {
            return null;
          }

          @Override
          public void removeUserFromRoom(
              User currentUser,
              DisplayUser userToRemove,
              LoginUserDataAccessInterface userDAO,
              Room room) {}

          @Override
          public void changeRoomName(
              User user, LoginUserDataAccessInterface userDAO, Room activeRoom, String roomName) {
            throw new RuntimeException("Failed to change room name.");
          }
        };

    RoomSettingsInteractor roomSettingsInteractor =
        new RoomSettingsInteractor(
            roomsDataAccessObject, userDataAccessObject, roomSettingsPresenter);

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
    roomSettingsViewModel.setUser(DAOTest.createDummyUser());

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
