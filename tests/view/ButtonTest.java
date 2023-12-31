package view;

import static java.lang.Thread.sleep;

import interface_adapter.ViewManagerModel;
import interface_adapter.switch_view.SwitchViewController;
import interface_adapter.switch_view.SwitchViewPresenter;
import use_case.switch_view.SwitchViewInputBoundary;
import use_case.switch_view.SwitchViewInteractor;
import use_case.switch_view.SwitchViewOutputBoundary;

/** Helper class for testing switch view and button clicks */
public abstract class ButtonTest {
  SwitchViewController initializeSwitchViewController(ViewManagerModel viewManagerModel) {
    SwitchViewOutputBoundary switchViewOutputBoundary = new SwitchViewPresenter(viewManagerModel);
    SwitchViewInputBoundary switchViewInteractor =
        new SwitchViewInteractor(switchViewOutputBoundary);
    return new SwitchViewController(switchViewInteractor);
  }

  boolean checkActiveView(ViewManagerModel viewManagerModel, String expectedViewName) {
    try {
      sleep(100);
    } catch (InterruptedException e) {
      throw new RuntimeException(e);
    }

    return viewManagerModel.getActiveView().equals(expectedViewName);
  }
}
