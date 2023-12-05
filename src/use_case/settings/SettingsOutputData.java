package use_case.settings;


import entities.settings.SettingsChangeResponse;

public class SettingsOutputData {

  private SettingsChangeResponse response;

  public SettingsOutputData(SettingsChangeResponse response) {
    this.response = response;
  }

  public SettingsChangeResponse getResponse(){return response;}


}
