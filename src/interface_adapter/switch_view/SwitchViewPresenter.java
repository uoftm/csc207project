package interface_adapter.switch_view;

import interface_adapter.ViewManagerModel;
import use_case.switch_view.SwitchViewOutputBoundary;

public class SwitchViewPresenter implements SwitchViewOutputBoundary {

  private final ViewManagerModel viewManagerModel;

  public SwitchViewPresenter(ViewManagerModel viewManagerModel) {
    this.viewManagerModel = viewManagerModel;
  }

  @Override
  public void present(String outputData) {
    // Logic to handle view switching
    this.viewManagerModel.setActiveView(outputData);
  }
}
