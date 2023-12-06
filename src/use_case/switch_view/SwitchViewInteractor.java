package use_case.switch_view;

public class SwitchViewInteractor implements SwitchViewInputBoundary {
  private final SwitchViewOutputBoundary outputBoundary;

  public SwitchViewInteractor(SwitchViewOutputBoundary outputBoundary) {
    this.outputBoundary = outputBoundary;
  }

  @Override
  public void execute(SwitchViewInputData inputData) {
    // Logic to switch views
    // For example, sending the view name to the presenter
    SwitchViewOutputData outputData = new SwitchViewOutputData(inputData.viewName());
    outputBoundary.present(outputData);
  }
}
