package view;

import app.RoomsUseCaseFactory;
import app.SearchUseCaseFactory;
import data_access.ElasticsearchDataAccessObject;
import data_access.FirebaseMessageDataAccessObject;
import data_access.FirebaseRoomsDataAccessObject;
import data_access.FirebaseUserDataAccessObject;
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
import use_case.search.SearchDataAccessInterface;

public class RoomsTest extends ButtonTest {

  @Test
  public void testClickingRefresh() {
    RoomsViewModel roomsViewModel = new RoomsViewModel();
    OkHttpClient client = new OkHttpClient();
    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            new FirebaseRoomsDataAccessObject(client),
            new FirebaseMessageDataAccessObject(client),
            new FirebaseUserDataAccessObject(client),
            roomsViewModel,
            null,
            null,
            null);

    roomsViewModel.setState(this.buildTestState());

    JButton refresh = roomsView.getRefreshButton();
    refresh.doClick();

    // User can load blank rooms with request to Firebase
    Assert.assertNull(roomsViewModel.getState().getError());
  }

  @Test
  public void testRoomCreation() {
    RoomsViewModel roomsViewModel = new RoomsViewModel();
    OkHttpClient client = new OkHttpClient();
    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            new FirebaseRoomsDataAccessObject(client),
            new FirebaseMessageDataAccessObject(client),
            new FirebaseUserDataAccessObject(client),
            roomsViewModel,
            null,
            null,
            null);

    RoomsState testState = buildTestState();
    testState.setRoomToCreateName("Test Room!");
    roomsViewModel.setState(testState);

    JButton createRoomButton = roomsView.getCreateRoomButton();
    createRoomButton.doClick();

    String regex = "^Authentication failed: .*";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(roomsViewModel.getState().getError());
    Assert.assertTrue(matcher.find());
  }

  @Test
  public void testSendMessage() {
    OkHttpClient client = new OkHttpClient();
    ViewManagerModel viewManagerModel = new ViewManagerModel();

    SearchViewModel searchViewModel = new SearchViewModel();
    SearchedViewModel searchedViewModel = new SearchedViewModel();
    SearchDataAccessInterface searchDataAccessObject = new ElasticsearchDataAccessObject(client);
    SearchController searchController =
        SearchUseCaseFactory.createSearchController(
            searchViewModel, searchDataAccessObject, viewManagerModel, searchedViewModel);

    RoomsViewModel roomsViewModel = new RoomsViewModel();
    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            new FirebaseRoomsDataAccessObject(client),
            new FirebaseMessageDataAccessObject(client),
            new FirebaseUserDataAccessObject(client),
            roomsViewModel,
            searchController,
            null,
            null);

    RoomsState testState = buildTestState();
    testState.setRoomUid(testState.getAvailableRooms().get(0).getUid());
    testState.setSendMessage("Test message!");
    roomsViewModel.setState(testState);

    JButton sendMessageButton = roomsView.getSendMessageButton();
    sendMessageButton.doClick();

    Assert.assertNotNull(roomsViewModel.getState().getError());
  }

  @Test
  public void testAddUserToRoom() {
    RoomsViewModel roomsViewModel = new RoomsViewModel();
    OkHttpClient client = new OkHttpClient();
    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            new FirebaseRoomsDataAccessObject(client),
            new FirebaseMessageDataAccessObject(client),
            new FirebaseUserDataAccessObject(client),
            roomsViewModel,
            null,
            null,
            null);

    RoomsState testState = buildTestState();
    testState.setUserToAddEmail(createDummyDisplayUser().getEmail());
    roomsViewModel.setState(testState);

    JButton addUserButton = roomsView.getAddUserButton();
    addUserButton.doClick();

    String regex =
        "Unable to add \\s*([a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,})\\s*to room:";

    Pattern pattern = Pattern.compile(regex);
    Matcher matcher = pattern.matcher(roomsViewModel.getState().getError());
    Assert.assertTrue(matcher.find());
  }

  private RoomsState buildTestState() {
    RoomsState state = new RoomsState();
    state.setUser(this.createDummyUser());
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
    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            new FirebaseRoomsDataAccessObject(client),
            new FirebaseMessageDataAccessObject(client),
            new FirebaseUserDataAccessObject(client),
            roomsViewModel,
            null,
            startSearchController,
            null);
    RoomsState currentstate = roomsViewModel.getState();
    currentstate.setUser(createDummyUser());
    currentstate.setRoomUid(createDummyRoom().getUid());
    roomsViewModel.setState(currentstate);
    JButton searchButton = roomsView.getSearchButton();
    searchButton.doClick();
  }
}
