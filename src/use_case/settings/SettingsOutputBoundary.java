package use_case.settings;

public interface SettingsOutputBoundary {
  void prepareSuccessView(SettingsOutputData outputData);

  void prepareFailView(SettingsOutputData outputData);
}
