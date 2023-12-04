package use_case.settings;


public class SettingsOutputData {

  private String error;
  private Boolean isError;

  public SettingsOutputData(String error, Boolean isError) {
    this.error = error;
    this.isError = isError;
  }

  public String getError(){return error;}

  public Boolean getIsError(){return isError;}


}
