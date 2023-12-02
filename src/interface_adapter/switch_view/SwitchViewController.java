package interface_adapter.switch_view;

import use_case.switch_view.SwitchViewInputBoundary;

public class SwitchViewController {
  private final SwitchViewInputBoundary switchViewInteractor;

  public SwitchViewController(SwitchViewInputBoundary switchViewInteractor) {
    this.switchViewInteractor = switchViewInteractor;
  }

  /**
   * Switch the currently displayed page to the given view.
   *
   * @param viewName The name of the view to switch to. (must be added to the view manager first)
   */
  public void switchTo(String viewName) {
    switchViewInteractor.execute(viewName);
  }
}
