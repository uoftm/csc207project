package use_case.settings;

public interface SettingsInputBoundary {
  void execute(SettingsInputData settingsInputData);

  void executeChangeUsername(SettingsInputData settingsInputData);
}
