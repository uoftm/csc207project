package view;

import app.LoginUseCaseFactory;
import data_access.DAOTest;
import data_access.FirebaseUserDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import entities.auth.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.rooms.RoomsViewModel;
import javax.swing.*;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import use_case.rooms.LoggedInDataAccessInterface;

public class LoginTest extends ButtonTest {

  @Test
  public void testClickingLoginButton() {
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    LoginViewModel loginViewModel = new LoginViewModel();
    OkHttpClient client = new OkHttpClient();
    LoggedInDataAccessInterface inMemoryDAO = new InMemoryUserDataAccessObject();
    LoginView loginView =
        LoginUseCaseFactory.create(
            viewManagerModel,
            loginViewModel,
            null,
            null,
            new FirebaseUserDataAccessObject(client),
            inMemoryDAO,
            initializeSwitchViewController(viewManagerModel));

    JButton login = loginView.getLoginButton();
    login.doClick();

    Assert.assertNotNull(loginViewModel.getState().getError());
  }

  @Test
  public void testClickingCancelButton() {
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    LoginViewModel loginViewModel = new LoginViewModel();
    LoggedInDataAccessInterface inMemoryDAO = new InMemoryUserDataAccessObject();
    LoginView loginView =
        LoginUseCaseFactory.create(
            viewManagerModel,
            loginViewModel,
            null,
            null,
            null,
            inMemoryDAO,
            initializeSwitchViewController(viewManagerModel));

    JButton cancel = loginView.getCancelButton();
    cancel.doClick();

    Assert.assertTrue(checkActiveView(viewManagerModel, WelcomeView.viewName));
  }

  @Test
  public void testLoggingIn() {
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    LoginViewModel loginViewModel = new LoginViewModel();
    OkHttpClient client = new OkHttpClient();
    LoggedInDataAccessInterface inMemoryDAO = new InMemoryUserDataAccessObject();
    var x = new FirebaseUserDataAccessObject(client);

    LoginView loginView =
        LoginUseCaseFactory.create(
            viewManagerModel,
            loginViewModel,
            new LoggedInViewModel(),
            new RoomsViewModel(),
            x,
            inMemoryDAO,
            initializeSwitchViewController(viewManagerModel));

    User user = DAOTest.addFirebaseDummyUser();

    JTextField email = loginView.getEmailField();
    email.setText(user.getEmail());
    JPasswordField password = loginView.getPasswordField();
    password.setText(user.getPassword());

    JButton login = loginView.getLoginButton();
    login.doClick();

    Assert.assertNull(loginViewModel.getState().getError());
    DAOTest.cleanUpUser(user);
  }
}
