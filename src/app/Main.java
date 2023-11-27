package app;

import data_access.FirebaseMessageDataAccessObject;
import data_access.FirebaseUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import javax.swing.*;
import okhttp3.OkHttpClient;
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
    LoginViewModel loginViewModel = new LoginViewModel();
    LoggedInViewModel loggedInViewModel = new LoggedInViewModel();
    SignupViewModel signupViewModel = new SignupViewModel();

    FirebaseUserDataAccessObject userDataAccessObject = new FirebaseUserDataAccessObject();

    SwitchViewController switchViewController = SwitchViewUseCaseFactory.create(viewManagerModel);

    SignupView signupView =
        SignupUseCaseFactory.create(
            viewManagerModel,
            loginViewModel,
            signupViewModel,
            userDataAccessObject,
            switchViewController);
    views.add(signupView.contentPane, SignupView.viewName);

    LoginView loginView =
        LoginUseCaseFactory.create(
            viewManagerModel,
            loginViewModel,
            loggedInViewModel,
            userDataAccessObject,
            switchViewController);
    views.add(loginView.contentPane, loginView.viewName);

    WelcomeView welcomeView = new WelcomeView(switchViewController);
    views.add(welcomeView.contentPane, WelcomeView.viewName);

    OkHttpClient client = new OkHttpClient();
    var messageDataAccessObject = new FirebaseMessageDataAccessObject(client);

    ChatView chatView = ChatUseCaseFactory.create(messageDataAccessObject);
    LoggedInView loggedInView = new LoggedInView(loggedInViewModel, chatView, switchViewController);
    views.add(loggedInView.contentPane, loggedInView.viewName);

    viewManagerModel.setActiveView(WelcomeView.viewName);
    viewManagerModel.firePropertyChanged();

    application.pack();
    application.setVisible(true);
  }
}
