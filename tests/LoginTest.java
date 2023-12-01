import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

import app.LoginUseCaseFactory;
import data_access.FirebaseUserDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginController;
import interface_adapter.login.LoginViewModel;
import interface_adapter.switch_view.SwitchViewController;
import interface_adapter.switch_view.SwitchViewPresenter;
import javax.swing.*;

import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import use_case.switch_view.SwitchViewInputBoundary;
import use_case.switch_view.SwitchViewInteractor;
import use_case.switch_view.SwitchViewOutputBoundary;
import view.LoginView;
import view.SignupView;
import view.WelcomeView;

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
            new FirebaseUserDataAccessObject(client),
            initializeSwitchViewController(viewManagerModel));

    JButton login = loginView.getLoginButton();
    login.doClick();

    Assert.assertTrue(loginViewModel.getState().getError() != null);
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
                    initializeSwitchViewController(viewManagerModel));

    JButton cancel = loginView.getCancelButton();
    cancel.doClick();

    Assert.assertTrue(checkActiveView(viewManagerModel, WelcomeView.viewName));
  }
}
