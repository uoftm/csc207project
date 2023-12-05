package entities.settings;

public class SettingsChangeResponse {
    private final String error;
    private final Boolean isError;

    public SettingsChangeResponse(String error, Boolean isError) {
        this.error = error;
        this.isError = isError;
    }

    public String getError(){return error;}

    public Boolean getIsError(){return isError;}
}
