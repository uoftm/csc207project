package use_case.settings;

public interface SettingsOutputBoundary {
  void prepareSuccessView(SettingsOutputData user);

  void prepareFailView(String error);
}
