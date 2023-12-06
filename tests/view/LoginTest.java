package view;

import app.LoginUseCaseFactory;
import data_access.FirebaseUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import javax.swing.*;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;

public class LoginTest extends ButtonTest {

  @Test
  public void testClickingLoginButton() {
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    LoginViewModel loginViewModel = new LoginViewModel();
    OkHttpClient client = new OkHttpClient();
    LoginView loginView =
        LoginUseCaseFactory.create(
            viewManagerModel,
            loginViewModel,
            null,
            null,
            new FirebaseUserDataAccessObject(client),
            initializeSwitchViewController(viewManagerModel));

    JButton login = loginView.getLoginButton();
    login.doClick();

    Assert.assertNotNull(loginViewModel.getState().getError());
  }

  @Test
  public void testClickingCancelButton() {
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    LoginViewModel loginViewModel = new LoginViewModel();
    LoginView loginView =
        LoginUseCaseFactory.create(
            viewManagerModel,
            loginViewModel,
            null,
            null,
            null,
            initializeSwitchViewController(viewManagerModel));

    JButton cancel = loginView.getCancelButton();
    cancel.doClick();

    Assert.assertTrue(checkActiveView(viewManagerModel, WelcomeView.viewName));
  }
}
