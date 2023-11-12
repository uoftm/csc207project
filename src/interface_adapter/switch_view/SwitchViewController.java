package interface_adapter.switch_view;

import use_case.switch_view.SwitchViewInputBoundary;
import use_case.switch_view.SwitchViewInputData;

public class SwitchViewController {

  private final SwitchViewInputBoundary switchViewInteractor;

  public SwitchViewController(SwitchViewInputBoundary switchViewInteractor) {
    this.switchViewInteractor = switchViewInteractor;
  }

  public void switchTo(String viewName) {
    SwitchViewInputData switchViewInputData = new SwitchViewInputData(viewName);
    switchViewInteractor.execute(switchViewInputData);
  }
}
