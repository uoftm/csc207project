package interface_adapter.switch_view;

import use_case.switch_view.SwitchViewInputBoundary;

public class SwitchViewController {
  private final SwitchViewInputBoundary switchViewInteractor;

  public SwitchViewController(SwitchViewInputBoundary switchViewInteractor) {
    this.switchViewInteractor = switchViewInteractor;
  }

  public void switchTo(String viewName) {
    switchViewInteractor.execute(viewName);
  }
}
