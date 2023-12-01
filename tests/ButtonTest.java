import interface_adapter.ViewManagerModel;
import interface_adapter.switch_view.SwitchViewController;
import interface_adapter.switch_view.SwitchViewPresenter;
import use_case.switch_view.SwitchViewInputBoundary;
import use_case.switch_view.SwitchViewInteractor;
import use_case.switch_view.SwitchViewOutputBoundary;
import view.LoginView;
import view.SignupView;

import javax.swing.*;
import javax.swing.text.View;

import static java.lang.Thread.sleep;
import static org.junit.Assert.assertEquals;

public abstract class ButtonTest {
    SwitchViewController initializeSwitchViewController(ViewManagerModel viewManagerModel) {
        SwitchViewOutputBoundary switchViewOutputBoundary = new SwitchViewPresenter(viewManagerModel);
        SwitchViewInputBoundary switchViewInteractor =
                new SwitchViewInteractor(switchViewOutputBoundary);
        return new SwitchViewController(switchViewInteractor);
    }

    boolean checkActiveView(ViewManagerModel viewManagerModel, String expectedViewName) {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return viewManagerModel.getActiveView().equals(expectedViewName);
    }
}
