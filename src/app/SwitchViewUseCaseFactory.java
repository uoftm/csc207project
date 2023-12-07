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

  /**
   * Creates the SwitchViewController and helper classes and return
   *
   * @return The instance of the SwitchViewController
   */
  public static SwitchViewController create(ViewManagerModel viewManagerModel) {
    SwitchViewOutputBoundary switchViewOutputBoundary = new SwitchViewPresenter(viewManagerModel);
    SwitchViewInputBoundary switchViewInteractor =
        new SwitchViewInteractor(switchViewOutputBoundary);

    return new SwitchViewController(switchViewInteractor);
  }
}
