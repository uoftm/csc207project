package app;

import interface_adapter.ViewManagerModel;
import interface_adapter.switch_view.SwitchViewController;
import interface_adapter.switch_view.SwitchViewPresenter;
import use_case.switch_view.SwitchViewInputBoundary;
import use_case.switch_view.SwitchViewInteractor;
import use_case.switch_view.SwitchViewOutputBoundary;

public class SwitchViewUseCaseFactory {
  private static SwitchViewOutputBoundary presenter;
  private static SwitchViewController controller;

  /** Prevent instantiation. */
  private SwitchViewUseCaseFactory() {}

  public static void initialize(ViewManagerModel viewManagerModel) {
    presenter = new SwitchViewPresenter(viewManagerModel);
    SwitchViewInputBoundary switchViewInteractor = new SwitchViewInteractor(presenter);
    controller = new SwitchViewController(switchViewInteractor);
  }

  public static SwitchViewOutputBoundary getPresenter() {
    return presenter;
  }

  public static SwitchViewController getController() {
    return controller;
  }
}
