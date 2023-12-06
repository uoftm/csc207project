package app;

import data_access.*;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.room_settings.OpenRoomSettingsController;
import interface_adapter.room_settings.RoomSettingsController;
import interface_adapter.room_settings.RoomSettingsPresenter;
import interface_adapter.room_settings.RoomSettingsViewModel;
import interface_adapter.rooms.RoomsViewModel;
import interface_adapter.search.SearchController;
import interface_adapter.search.SearchViewModel;
import interface_adapter.search.StartSearchController;
import interface_adapter.searched.SearchedViewModel;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import javax.swing.*;
import okhttp3.OkHttpClient;
import use_case.room_settings.RoomSettingsInteractor;
import use_case.room_settings.RoomSettingsOutputBoundary;
import use_case.rooms.LoggedInDataAccessInterface;
import use_case.rooms.MessageDataAccessInterface;
import use_case.search.SearchDataAccessInterface;
import view.*;

public class Main {
  public static void main(String[] args) {
    // Build the main program window, the main panel containing the
    // various cards, and the layout, and stitch them together.

    // The main application window.
    JFrame application = new JFrame("MSGR");
    // Load main window icon
    ImageIcon icon = new ImageIcon("assets/images/MSGR_Icon.png");
    Image image = icon.getImage();
    application.setIconImage(image);

    application.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    // This keeps track of and manages which view is currently showing.
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    ViewManager views = new ViewManager(viewManagerModel);
    application.add(views.getViews());

    // The data for the views, such as username and password, are in the ViewModels.
    // This information will be changed by a presenter object that is reporting the
    // results from the use case. The ViewModels are observable, and will
    // be observed by the Views.
    SignupViewModel signupViewModel = new SignupViewModel();
    LoginViewModel loginViewModel = new LoginViewModel();
    LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
    SettingsViewModel settingsViewModel = new SettingsViewModel();
    RoomsViewModel roomsViewModel = new RoomsViewModel();

    OkHttpClient client = new OkHttpClient();
    FirebaseUserDataAccessObject userDataAccessObject = new FirebaseUserDataAccessObject(client);
    FirebaseRoomsDataAccessObject roomsDataAccessObject = new FirebaseRoomsDataAccessObject(client);

    LoggedInDataAccessInterface inMemoryDAO = new InMemoryUserDataAccessObject();

    SwitchViewController switchViewController = SwitchViewUseCaseFactory.create(viewManagerModel);

    SignupView signupView =
        SignupUseCaseFactory.create(
            viewManagerModel,
            loginViewModel,
            signupViewModel,
            userDataAccessObject,
            switchViewController);
    viewManagerModel.add(signupView.contentPane, SignupView.viewName);

    LoginView loginView =
        LoginUseCaseFactory.create(
            viewManagerModel,
            loginViewModel,
            loggedInViewModel,
            roomsViewModel,
            userDataAccessObject,
            inMemoryDAO,
            switchViewController);
    viewManagerModel.add(loginView.contentPane, loginView.viewName);

    WelcomeView welcomeView = new WelcomeView(switchViewController);
    viewManagerModel.add(welcomeView.contentPane, WelcomeView.viewName);
    MessageDataAccessInterface messageDataAccessObject =
        new FirebaseMessageDataAccessObject(client);

    SearchViewModel searchViewModel = new SearchViewModel();
    SearchedViewModel searchedViewModel = new SearchedViewModel();
    SearchDataAccessInterface searchDataAccessObject = new ElasticsearchDataAccessObject(client);
    SearchController searchController =
        SearchUseCaseFactory.createSearchController(
            searchViewModel,
            searchDataAccessObject,
            inMemoryDAO,
            viewManagerModel,
            searchedViewModel);

    var searchView = new SearchView(searchController, searchViewModel, switchViewController);
    viewManagerModel.add(searchView.contentPane, SearchView.viewName);

    var searchedView = new SearchedView(searchedViewModel, switchViewController);
    viewManagerModel.add(searchedView.contentPane, SearchedView.viewName);

    StartSearchController startSearchController =
        new StartSearchController(viewManagerModel, searchViewModel);

    RoomSettingsViewModel roomSettingsViewModel = new RoomSettingsViewModel();
    OpenRoomSettingsController openRoomSettingsController =
        new OpenRoomSettingsController(roomSettingsViewModel, viewManagerModel);

    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            roomsDataAccessObject,
            messageDataAccessObject,
            userDataAccessObject,
            inMemoryDAO,
            roomsViewModel,
            searchController,
            startSearchController,
            openRoomSettingsController);

    LoggedInView loggedInView =
        LoggedInUseCaseFactory.create(
            loggedInViewModel, inMemoryDAO, roomsView, switchViewController);
    viewManagerModel.add(loggedInView.contentPane, loggedInView.viewName);

    SettingsView settingsView =
        SettingsUseCaseFactory.create(
            settingsViewModel,
            loggedInViewModel,
            userDataAccessObject,
            roomsDataAccessObject,
            userDataAccessObject,
            inMemoryDAO,
            switchViewController);
    viewManagerModel.add(settingsView.contentPane, settingsView.viewName);

    RoomSettingsOutputBoundary outputBoundary =
        new RoomSettingsPresenter(roomsViewModel, roomSettingsViewModel, viewManagerModel);
    RoomSettingsInteractor roomSettingsInteractor =
        new RoomSettingsInteractor(
            roomsDataAccessObject, userDataAccessObject, inMemoryDAO, outputBoundary);
    RoomSettingsController roomSettingsController =
        new RoomSettingsController(roomSettingsInteractor);

    RoomSettingsView roomSettingsView =
        new RoomSettingsView(roomSettingsViewModel, roomSettingsController, switchViewController);
    viewManagerModel.add(roomSettingsView.contentPane, RoomSettingsView.viewName);

    viewManagerModel.setActiveView(WelcomeView.viewName);

    application.pack();
    application.setVisible(true);
  }
}
