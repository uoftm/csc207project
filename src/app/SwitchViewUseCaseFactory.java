package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.switch_view.SwitchViewController;
import interface_adapter.switch_view.SwitchViewPresenter;
import use_case.switch_view.SwitchViewInputBoundary;
import use_case.switch_view.SwitchViewInteractor;
import use_case.switch_view.SwitchViewOutputBoundary;

public class SwitchViewUseCaseFactory {

  /** Prevent instantiation. */
  private SwitchViewUseCaseFactory() {}

  public static SwitchViewController create(ViewManagerModel viewManagerModel) {
    SwitchViewOutputBoundary switchViewOutputBoundary = new SwitchViewPresenter(viewManagerModel);
    SwitchViewInputBoundary switchViewInteractor =
        new SwitchViewInteractor(switchViewOutputBoundary);

    return new SwitchViewController(switchViewInteractor);
  }
}
