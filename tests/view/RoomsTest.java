package view;

import app.RoomsUseCaseFactory;
import app.SearchUseCaseFactory;
import data_access.*;
import entities.auth.DisplayUser;
import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import interface_adapter.ViewManagerModel;
import interface_adapter.rooms.RoomsState;
import interface_adapter.rooms.RoomsViewModel;
import interface_adapter.search.SearchController;
import interface_adapter.search.SearchViewModel;
import interface_adapter.search.StartSearchController;
import interface_adapter.searched.SearchedViewModel;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.*;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.LoggedInDataAccessInterface;
import use_case.rooms.MessageDataAccessInterface;
import use_case.rooms.RoomsDataAccessInterface;
import use_case.search.SearchDataAccessInterface;
import use_case.settings.DeleteUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class RoomsTest extends ButtonTest {
  @Test
  public void testClickingRefreshSuccess() {
    OkHttpClient client = new OkHttpClient();
    ViewManagerModel viewManagerModel = new ViewManagerModel();

    MessageDataAccessInterface messageDao = new FirebaseMessageDataAccessObject(client);
    RoomsDataAccessInterface roomDao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    LoggedInDataAccessInterface inMemoryDao = new InMemoryUserDataAccessObject();
    User dummyUser = addFirebaseDummyUser();
    String idToken = userDao.getAccessToken(dummyUser.getEmail(), dummyUser.getPassword());
    inMemoryDao.setIdToken(idToken);
    inMemoryDao.setUser(dummyUser);
    Room dummyRoom = addFirebaseDummyRoom(idToken, dummyUser);

    SearchViewModel searchViewModel = new SearchViewModel();
    SearchedViewModel searchedViewModel = new SearchedViewModel();
    SearchDataAccessInterface searchDataAccessObject = new ElasticsearchDataAccessObject(client);
    SearchController searchController =
        SearchUseCaseFactory.createSearchController(
            searchViewModel,
            searchDataAccessObject,
            inMemoryDao,
            viewManagerModel,
            searchedViewModel);

    RoomsViewModel roomsViewModel = new RoomsViewModel();
    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            roomDao,
            messageDao,
            userDao,
            inMemoryDao,
            roomsViewModel,
            searchController,
            null,
            null);

    RoomsState testState = buildTestState(inMemoryDao);
    testState.setRoomUid(dummyRoom.getUid());
    List<Room> rooms = testState.getAvailableRooms();
    rooms.add(dummyRoom);
    testState.setAvailableRooms(rooms);
    roomsViewModel.setState(testState);

    JButton refreshButton = roomsView.getRefreshButton();
    refreshButton.doClick();

    Assert.assertNull(roomsViewModel.getState().getError());
    Assert.assertTrue(roomsViewModel.getState().getAvailableRooms().size() == 1);

    // Remove from Firebase
    cleanUpRoom(idToken, dummyRoom, dummyUser);
    cleanUpUser(idToken, dummyUser);
  }

  @Test
  public void testClickingRefreshFailed() {
    RoomsViewModel roomsViewModel = new RoomsViewModel();
    OkHttpClient client = new OkHttpClient();

    User dummyUser = addFirebaseDummyUser();
    LoggedInDataAccessInterface inMemoryDAO = new InMemoryUserDataAccessObject();
    inMemoryDAO.setUser(dummyUser);
    inMemoryDAO.setIdToken("fake token");

    RoomsView roomsView =
        RoomsUseCaseFactory.create(
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
              public void deleteRoom(String idToken, User user, Room room) {}

              @Override
              public void addUserToRoom(
                  String idToken, User currentUser, DisplayUser newUser, Room room) {}

              @Override
              public List<String> getAvailableRoomIds(User user) {
                throw new RuntimeException("Unable to get available rooms. Please try again.");
              }

              @Override
              public void removeUserFromRoom(
                  String idToken, User currentUser, DisplayUser userToRemove, Room room) {}

              @Override
              public void changeRoomName(
                  String idToken, User user, Room activeRoom, String roomName) {}
            },
            new FirebaseMessageDataAccessObject(client),
            new FirebaseUserDataAccessObject(client),
            inMemoryDAO,
            roomsViewModel,
            null,
            null,
            null);

    RoomsState testState = buildTestState(inMemoryDAO);
    inMemoryDAO.setUser(
        new User(
            "",
            dummyUser.getEmail(),
            dummyUser.getName(),
            dummyUser.getPassword(),
            dummyUser.getCreationTime()));
    roomsViewModel.setState(testState);

    JButton refresh = roomsView.getRefreshButton();
    refresh.doClick();

    // User can't load blank rooms with incorrect call to Firebase
    Assert.assertNotNull(roomsViewModel.getState().getError());
  }

  @Test
  public void testRoomCreationSuccess() {
    OkHttpClient client = new OkHttpClient();
    RoomsDataAccessInterface dao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    LoggedInDataAccessInterface inMemoryDao = new InMemoryUserDataAccessObject();
    User dummyUser = addFirebaseDummyUser();
    String idToken = userDao.getAccessToken(dummyUser.getEmail(), dummyUser.getPassword());
    inMemoryDao.setUser(dummyUser);
    inMemoryDao.setIdToken(idToken);

    RoomsViewModel roomsViewModel = new RoomsViewModel();
    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            dao,
            new FirebaseMessageDataAccessObject(client),
            userDao,
            inMemoryDao,
            roomsViewModel,
            null,
            null,
            null);

    RoomsState testState = buildTestState(inMemoryDao);
    testState.setRoomToCreateName("New Room");
    inMemoryDao.setUser(dummyUser);
    roomsViewModel.setState(testState);

    JButton createRoomButton = roomsView.getCreateRoomButton();
    createRoomButton.doClick();

    Assert.assertNull(testState.getError());

    // Remove from Firebase
    String roomUid = testState.getRoomUid();
    Assert.assertNotNull(roomUid);
    Room room = testState.getRoomByUid();
    cleanUpRoom(idToken, room, dummyUser);
    cleanUpUser(idToken, dummyUser);
  }

  @Test
  public void testRoomCreationFailed() {
    RoomsViewModel roomsViewModel = new RoomsViewModel();
    OkHttpClient client = new OkHttpClient();
    LoggedInDataAccessInterface inMemoryDAO = new InMemoryUserDataAccessObject();
    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            new FirebaseRoomsDataAccessObject(client),
            new FirebaseMessageDataAccessObject(client),
            new FirebaseUserDataAccessObject(client),
            inMemoryDAO,
            roomsViewModel,
            null,
            null,
            null);

    RoomsState testState = buildTestState(inMemoryDAO);
    testState.setRoomToCreateName("Test Room!");
    roomsViewModel.setState(testState);

    JButton createRoomButton = roomsView.getCreateRoomButton();
    createRoomButton.doClick();

    String regex = "^User not logged in.*";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(roomsViewModel.getState().getError());
    Assert.assertTrue(matcher.find());
  }

  @Test
  public void testSendMessageSuccess() {
    OkHttpClient client = new OkHttpClient();
    ViewManagerModel viewManagerModel = new ViewManagerModel();

    MessageDataAccessInterface messageDao = new FirebaseMessageDataAccessObject(client);
    RoomsDataAccessInterface roomDao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    LoggedInDataAccessInterface inMemoryDao = new InMemoryUserDataAccessObject();
    User dummyUser = addFirebaseDummyUser();
    String idToken = userDao.getAccessToken(dummyUser.getEmail(), dummyUser.getPassword());
    inMemoryDao.setUser(dummyUser);
    inMemoryDao.setIdToken(idToken);
    Room dummyRoom = addFirebaseDummyRoom(idToken, dummyUser);

    SearchViewModel searchViewModel = new SearchViewModel();
    SearchedViewModel searchedViewModel = new SearchedViewModel();
    SearchDataAccessInterface searchDataAccessObject = new ElasticsearchDataAccessObject(client);
    SearchController searchController =
        SearchUseCaseFactory.createSearchController(
            searchViewModel,
            searchDataAccessObject,
            inMemoryDao,
            viewManagerModel,
            searchedViewModel);

    RoomsViewModel roomsViewModel = new RoomsViewModel();
    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            roomDao,
            messageDao,
            userDao,
            inMemoryDao,
            roomsViewModel,
            searchController,
            null,
            null);

    RoomsState testState = buildTestState(inMemoryDao);
    String messageContent = "Test Message!";
    testState.setSendMessage(messageContent);
    inMemoryDao.setUser(dummyUser);
    testState.setRoomUid(dummyRoom.getUid());
    List<Room> rooms = testState.getAvailableRooms();
    rooms.add(dummyRoom);
    testState.setAvailableRooms(rooms);
    roomsViewModel.setState(testState);

    JButton sendMessageButton = roomsView.getSendMessageButton();
    sendMessageButton.doClick();

    Assert.assertNull(roomsViewModel.getState().getError());
    Assert.assertTrue(
        roomsViewModel.getState().getDisplayMessages().get(0).content.equals("Test Message!"));

    // Remove from Firebase
    cleanUpRoom(idToken, dummyRoom, dummyUser);
    cleanUpUser(idToken, dummyUser);
  }

  @Test
  public void testSendMessageFailed() {
    OkHttpClient client = new OkHttpClient();
    ViewManagerModel viewManagerModel = new ViewManagerModel();

    SearchViewModel searchViewModel = new SearchViewModel();
    SearchedViewModel searchedViewModel = new SearchedViewModel();
    LoggedInDataAccessInterface inMemoryDao = new InMemoryUserDataAccessObject();
    SearchDataAccessInterface searchDataAccessObject = new ElasticsearchDataAccessObject(client);
    SearchController searchController =
        SearchUseCaseFactory.createSearchController(
            searchViewModel,
            searchDataAccessObject,
            inMemoryDao,
            viewManagerModel,
            searchedViewModel);

    RoomsViewModel roomsViewModel = new RoomsViewModel();
    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            new FirebaseRoomsDataAccessObject(client),
            new FirebaseMessageDataAccessObject(client),
            new FirebaseUserDataAccessObject(client),
            inMemoryDao,
            roomsViewModel,
            searchController,
            null,
            null);

    RoomsState testState = buildTestState(inMemoryDao);
    testState.setRoomUid(testState.getAvailableRooms().get(0).getUid());
    testState.setSendMessage("Test Message!");
    roomsViewModel.setState(testState);

    JButton sendMessageButton = roomsView.getSendMessageButton();
    sendMessageButton.doClick();

    Assert.assertNotNull(roomsViewModel.getState().getError());
  }

  @Test
  public void testAddUserToRoomSuccess() {
    OkHttpClient client = new OkHttpClient();
    FirebaseRoomsDataAccessObject dao = new FirebaseRoomsDataAccessObject(client);
    LoginUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    LoggedInDataAccessInterface inMemoryDao = new InMemoryUserDataAccessObject();
    User dummyUser = addFirebaseDummyUser();
    String idToken = userDao.getAccessToken(dummyUser.getEmail(), dummyUser.getPassword());
    Room dummyRoom = addFirebaseDummyRoom(idToken, dummyUser);
    inMemoryDao.setUser(dummyUser);
    inMemoryDao.setIdToken(idToken);

    User dummyUser2 = addFirebaseDummyUser();
    DisplayUser dummyDisplayUser2 = new DisplayUser(dummyUser2.getEmail(), dummyUser2.getName());
    String idToken2 = userDao.getAccessToken(dummyUser2.getEmail(), dummyUser2.getPassword());

    RoomsViewModel roomsViewModel = new RoomsViewModel();
    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            dao,
            new FirebaseMessageDataAccessObject(client),
            userDao,
            inMemoryDao,
            roomsViewModel,
            null,
            null,
            null);

    RoomsState testState = buildTestState(inMemoryDao);
    testState.setUserToAddEmail(dummyDisplayUser2.getEmail());
    inMemoryDao.setUser(dummyUser);
    testState.setRoomUid(dummyRoom.getUid());
    List<Room> rooms = testState.getAvailableRooms();
    rooms.add(dummyRoom);
    testState.setAvailableRooms(rooms);
    roomsViewModel.setState(testState);

    JButton addUserButton = roomsView.getAddUserButton();
    addUserButton.doClick();

    Assert.assertNull(roomsViewModel.getState().getError());

    // Remove from Firebase
    cleanUpRoom(idToken, dummyRoom, dummyUser);
    cleanUpUser(idToken, dummyUser);
    cleanUpUser(idToken2, dummyUser2);
  }

  @Test
  public void testAddUserToRoomFailed() {
    RoomsViewModel roomsViewModel = new RoomsViewModel();
    OkHttpClient client = new OkHttpClient();
    LoggedInDataAccessInterface inMemoryDAO = new InMemoryUserDataAccessObject();
    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            new FirebaseRoomsDataAccessObject(client),
            new FirebaseMessageDataAccessObject(client),
            new FirebaseUserDataAccessObject(client),
            inMemoryDAO,
            roomsViewModel,
            null,
            null,
            null);

    RoomsState testState = buildTestState(inMemoryDAO);
    testState.setUserToAddEmail(createDummyDisplayUser().getEmail());
    roomsViewModel.setState(testState);

    JButton addUserButton = roomsView.getAddUserButton();
    addUserButton.doClick();

    String regex =
            "Unable to add user to room:.*";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(roomsViewModel.getState().getError());
    Assert.assertTrue(matcher.find());
  }

  private RoomsState buildTestState(LoggedInDataAccessInterface inMemoryDAO) {
    RoomsState state = new RoomsState();
    List<Room> rooms = new ArrayList<>();
    Room room = createDummyRoom();
    rooms.add(room);
    state.setAvailableRooms(rooms);
    state.setRoomUid(room.getUid());
    return state;
  }

  User createDummyUser() {
    String fakeEmail =
        String.format("testSaveUser%s@example.com", UUID.randomUUID().toString().substring(0, 10));
    return new User(null, fakeEmail, "Dummy User", "password", LocalDateTime.now());
  }

  DisplayUser createDummyDisplayUser() {
    return new DisplayUser("dummy@example.com", "dummyUid");
  }

  Room createDummyRoom() {
    DisplayUser dummyDisplayUser = new DisplayUser("dummyUid", "Dummy User");
    List<DisplayUser> users = new ArrayList<>();
    users.add(dummyDisplayUser);
    List<Message> messages = new ArrayList<>();
    messages.add(new Message(Instant.now(), "Dummy message", dummyDisplayUser));
    Room room = new Room("dummyRoomUid", "Dummy Room", users, null);
    room.setMessages(messages);
    return room;
  }

  @Test
  public void testClickSearchButton() {
    RoomsViewModel roomsViewModel = new RoomsViewModel();
    OkHttpClient client = new OkHttpClient();
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    SearchViewModel searchViewModel = new SearchViewModel();
    StartSearchController startSearchController =
        new StartSearchController(viewManagerModel, searchViewModel);
    LoggedInDataAccessInterface inMemoryDAO = new InMemoryUserDataAccessObject();
    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            new FirebaseRoomsDataAccessObject(client),
            new FirebaseMessageDataAccessObject(client),
            new FirebaseUserDataAccessObject(client),
            new InMemoryUserDataAccessObject(),
            roomsViewModel,
            null,
            startSearchController,
            null);
    RoomsState currentstate = roomsViewModel.getState();
    inMemoryDAO.setUser(createDummyUser());
    currentstate.setRoomUid(createDummyRoom().getUid());
    roomsViewModel.setState(currentstate);
    JButton searchButton = roomsView.getSearchButton();
    searchButton.doClick();
  }

  // TODO: Restructure tests folder to import this (after search, settings tests are merged)
  User addFirebaseDummyUser() {
    /**
     * This adds a dummy user to Firebase and returns that user The caller *must* clean up the user
     * after use
     */
    User dummyUser = createDummyUser();
    OkHttpClient client = new OkHttpClient();
    SignupUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    userDao.save(dummyUser);
    return dummyUser;
  }

  void cleanUpUser(String idToken, User user) {
    OkHttpClient client = new OkHttpClient();
    DeleteUserDataAccessInterface userDao = new FirebaseUserDataAccessObject(client);
    userDao.deleteUser(idToken, user);
  }

  void cleanUpRoom(String idToken, Room room, User user) {
    OkHttpClient client = new OkHttpClient();
    RoomsDataAccessInterface roomsDao = new FirebaseRoomsDataAccessObject(client);
    roomsDao.deleteRoom(idToken, user, room);
  }

  Room addFirebaseDummyRoom(String idToken, User user) {
    /**
     * This adds a dummy room to Firebase and returns that room The caller *must* clean up the room
     * after use
     */
    OkHttpClient client = new OkHttpClient();
    RoomsDataAccessInterface roomsDao = new FirebaseRoomsDataAccessObject(client);
    Room room = roomsDao.addRoom(idToken, user, "Dummy Room");
    return room;
  }

  Message createDummyMessage(DisplayUser dummyDisplayUser) {
    return new Message(Instant.now(), "Dummy message", dummyDisplayUser);
  }
}
