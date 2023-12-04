package app;

import data_access.FirebaseRoomsDataAccessObject;
import data_access.FirebaseSettingsDataAccessObject;
import data_access.FirebaseUserDataAccessObject;
import data_access.SearchDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
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
import use_case.rooms.RoomsDataAccessInterface;
import use_case.search.SearchDataAccessInterface;
import use_case.settings.SettingsDataAccessInterface;
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
            switchViewController);
    viewManagerModel.add(loginView.contentPane, loginView.viewName);

    WelcomeView welcomeView = new WelcomeView(switchViewController);
    viewManagerModel.add(welcomeView.contentPane, WelcomeView.viewName);

    RoomsDataAccessInterface roomsDataAccessObject = new FirebaseRoomsDataAccessObject(client);

    SearchViewModel searchViewModel = new SearchViewModel();
    SearchedViewModel searchedViewModel = new SearchedViewModel();
    SearchDataAccessInterface searchDataAccessObject = new SearchDataAccessObject(client);
    SearchController searchController =
        SearchUseCaseFactory.createSearchController(
            searchViewModel, searchDataAccessObject, viewManagerModel, searchedViewModel);

    var searchView = new SearchView(searchController, searchViewModel, switchViewController);
    viewManagerModel.add(searchView.contentPane, SearchView.viewName);

    var searchedView = new SearchedView(searchedViewModel, switchViewController);
    viewManagerModel.add(searchedView.contentPane, SearchedView.viewName);

    StartSearchController startSearchController =
        new StartSearchController(viewManagerModel, searchViewModel);

    RoomsView roomsView =
        RoomsUseCaseFactory.create(
            roomsDataAccessObject,
            userDataAccessObject,
            roomsViewModel,
            searchController,
            startSearchController);

    LoggedInView loggedInView =
        new LoggedInView(loggedInViewModel, roomsView, switchViewController);
    viewManagerModel.add(loggedInView.contentPane, loggedInView.viewName);

    SettingsDataAccessInterface settingsUserDataAccessObject =
        new FirebaseSettingsDataAccessObject();

    SettingsView settingsView =
        SettingsUseCaseFactory.create(
            settingsViewModel, settingsUserDataAccessObject, switchViewController);
    viewManagerModel.add(settingsView.contentPane, settingsView.viewName);

    viewManagerModel.setActiveView(WelcomeView.viewName);

    application.pack();
    application.setVisible(true);
  }
}
