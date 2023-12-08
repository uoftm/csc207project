package view;

import app.SignupUseCaseFactory;
import data_access.DAOTest;
import data_access.FirebaseUserDataAccessObject;
import entities.auth.User;
import interface_adapter.ViewManagerModel;
import interface_adapter.login.LoginViewModel;
import interface_adapter.signup.SignupViewModel;
import javax.swing.*;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;

public class SignupTest extends ButtonTest {

  @Test
  public void testClickingSignupButton() {
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    SignupViewModel signupViewModel = new SignupViewModel();
    OkHttpClient client = new OkHttpClient();
    SignupView signupView =
        SignupUseCaseFactory.create(
            viewManagerModel,
            null,
            signupViewModel,
            new FirebaseUserDataAccessObject(client),
            initializeSwitchViewController(viewManagerModel));

    JButton signup = signupView.getSignupButton();
    signup.doClick();

    // Check that our Firebase User Data Access Object returns an error (since we haven't provided
    // any sign in data)
    Assert.assertNotNull(signupViewModel.getState().getError());
  }

  @Test
  public void testClickingCancelButton() {
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    SignupViewModel signupViewModel = new SignupViewModel();
    SignupView signupView =
        SignupUseCaseFactory.create(
            viewManagerModel,
            null,
            signupViewModel,
            null,
            initializeSwitchViewController(viewManagerModel));

    JButton cancel = signupView.getCancelButton();
    cancel.doClick();

    Assert.assertTrue(checkActiveView(viewManagerModel, WelcomeView.viewName));
  }

  @Test
  public void testClickingSignupButtonWithDummyUser() {
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    SignupViewModel signupViewModel = new SignupViewModel();
    OkHttpClient client = new OkHttpClient();
    SignupView signupView =
        SignupUseCaseFactory.create(
            viewManagerModel,
            new LoginViewModel(),
            signupViewModel,
            new FirebaseUserDataAccessObject(client),
            initializeSwitchViewController(viewManagerModel));

    User user = DAOTest.createDummyUser();
    signupView.getEmailField().setText(user.getEmail());
    signupView.getUsernameField().setText(user.getName());
    signupView.getPasswordField().setText(user.getPassword());
    signupView.getRepeatPasswordField().setText(user.getPassword());

    JButton signup = signupView.getSignupButton();
    signup.doClick();

    Assert.assertNull(signupViewModel.getState().getError());

    DAOTest.cleanUpUser(user);
  }
}
