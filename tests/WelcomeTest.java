import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

import interface_adapter.ViewManagerModel;
import interface_adapter.switch_view.SwitchViewController;
import interface_adapter.switch_view.SwitchViewPresenter;
import javax.swing.*;
import org.junit.Test;
import use_case.switch_view.SwitchViewInputBoundary;
import use_case.switch_view.SwitchViewInteractor;
import use_case.switch_view.SwitchViewOutputBoundary;
import view.LoginView;
import view.SignupView;
import view.WelcomeView;

public class WelcomeTest {
  @Test
  public void testClickingLoginButton() {
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    SwitchViewOutputBoundary switchViewOutputBoundary = new SwitchViewPresenter(viewManagerModel);
    SwitchViewInputBoundary switchViewInteractor =
        new SwitchViewInteractor(switchViewOutputBoundary);
    SwitchViewController switchViewController = new SwitchViewController(switchViewInteractor);
    WelcomeView view = new WelcomeView(switchViewController);

    JFrame jf = new JFrame();
    jf.setContentPane(view.contentPane);
    jf.pack();
    jf.setVisible(true);

    JButton login = (JButton) view.contentPane.getComponent(0).getComponentAt(200, 140);
    login.doClick();

    try {
      sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    assertEquals(viewManagerModel.getActiveView(), LoginView.viewName);
  }

  @Test
  public void testClickingSignupButton() {
    ViewManagerModel viewManagerModel = new ViewManagerModel();
    SwitchViewOutputBoundary switchViewOutputBoundary = new SwitchViewPresenter(viewManagerModel);
    SwitchViewInputBoundary switchViewInteractor =
        new SwitchViewInteractor(switchViewOutputBoundary);
    SwitchViewController switchViewController = new SwitchViewController(switchViewInteractor);
    WelcomeView view = new WelcomeView(switchViewController);

    JFrame jf = new JFrame();
    jf.setContentPane(view.contentPane);
    jf.pack();
    jf.setVisible(true);

    JButton login = (JButton) view.contentPane.getComponent(0).getComponentAt(200, 240);
    login.doClick();

    try {
      sleep(1000);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    assertEquals(viewManagerModel.getActiveView(), SignupView.viewName);
  }
}
