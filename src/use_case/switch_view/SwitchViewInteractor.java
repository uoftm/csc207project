package use_case.switch_view;

public class SwitchViewInteractor implements SwitchViewInputBoundary {
  private final SwitchViewOutputBoundary outputBoundary;

  public SwitchViewInteractor(SwitchViewOutputBoundary outputBoundary) {
    this.outputBoundary = outputBoundary;
  }

  /**
   * A simple SwitchViewInteractor that just passes controller information straight to the
   * presenter.
   */
  @Override
  public void execute(String inputData) {
    // Logic to switch views
    // For example, sending the view name to the presenter
    outputBoundary.present(inputData);
  }
}
