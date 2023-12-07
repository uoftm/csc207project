package view;

import app.LoginUseCaseFactory;
import data_access.FirebaseUserDataAccessObject;
import data_access.InMemoryUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
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

    Assert.assertTrue(loginViewModel.getState().getError() != null);
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
}
