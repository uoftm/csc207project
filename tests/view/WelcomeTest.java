package view;

import interface_adapter.switch_view.ViewManagerModel;
import javax.swing.*;
import org.junit.Assert;
import org.junit.Test;

public class WelcomeTest extends ButtonTest {
  @Test
  public void testClickingLoginButton() {
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    WelcomeView view = new WelcomeView(initializeSwitchViewController(viewManagerModel));

    JButton login = view.getLoginButton();
    login.doClick();

    Assert.assertTrue(checkActiveView(viewManagerModel, LoginView.viewName));
  }

  @Test
  public void testClickingSignupButton() {
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    WelcomeView view = new WelcomeView(initializeSwitchViewController(viewManagerModel));

    JButton signup = view.getSignupButton();
    signup.doClick();

    Assert.assertTrue(checkActiveView(viewManagerModel, SignupView.viewName));
  }
}
