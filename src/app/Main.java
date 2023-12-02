package app;

import data_access.FirebaseRoomsDataAccessObject;
import data_access.FirebaseSettingsDataAccessObject;
import data_access.FirebaseUserDataAccessObject;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.rooms.RoomsViewModel;
import interface_adapter.settings.SettingsViewModel;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.switch_view.ViewManagerModel;
import java.awt.*;
import javax.swing.*;
import okhttp3.OkHttpClient;
import use_case.rooms.RoomsDataAccessInterface;
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

    CardLayout cardLayout = new CardLayout();

    // The various View objects. Only one view is visible at a time.
    JPanel views = new JPanel(cardLayout);
    application.add(views);

    // This keeps track of and manages which view is currently showing.
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    new ViewManager(views, cardLayout, viewManagerModel);

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

    SwitchViewUseCaseFactory.initialize(viewManagerModel);

    SignupView signupView =
        SignupUseCaseFactory.create(loginViewModel, signupViewModel, userDataAccessObject);
    views.add(signupView.contentPane, SignupView.viewName);

    LoginView loginView =
        LoginUseCaseFactory.create(
            loginViewModel, loggedInViewModel, roomsViewModel, userDataAccessObject);
    views.add(loginView.contentPane, loginView.viewName);

    WelcomeView welcomeView = new WelcomeView(SwitchViewUseCaseFactory.getController());
    views.add(welcomeView.contentPane, WelcomeView.viewName);

    RoomsDataAccessInterface roomsDataAccessObject = new FirebaseRoomsDataAccessObject();

    RoomsView roomsView = RoomsUseCaseFactory.create(roomsDataAccessObject, roomsViewModel);

    LoggedInView loggedInView =
        new LoggedInView(loggedInViewModel, roomsView, SwitchViewUseCaseFactory.getController());
    views.add(loggedInView.contentPane, loggedInView.viewName);

    SettingsDataAccessInterface settingsUserDataAccessObject =
        new FirebaseSettingsDataAccessObject();

    SettingsView settingsView =
        SettingsUseCaseFactory.create(settingsViewModel, settingsUserDataAccessObject);
    views.add(settingsView.contentPane, settingsView.viewName);

    viewManagerModel.setActiveView(WelcomeView.viewName);
    viewManagerModel.firePropertyChanged();

    application.pack();
    application.setVisible(true);
  }
}
