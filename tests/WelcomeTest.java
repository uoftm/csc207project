import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import interface_adapter.ViewManagerModel;
import interface_adapter.switch_view.SwitchViewController;
import interface_adapter.switch_view.SwitchViewPresenter;
import javax.swing.*;

import org.junit.Assert;
import org.junit.Test;
import use_case.switch_view.SwitchViewInputBoundary;
import use_case.switch_view.SwitchViewInteractor;
import use_case.switch_view.SwitchViewOutputBoundary;
import view.LoginView;
import view.SignupView;
import view.WelcomeView;

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
