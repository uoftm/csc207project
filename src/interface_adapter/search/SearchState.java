package interface_adapter.search;

public class SearchState {

  private String roomUid;

  private String error;

  private Boolean isError;

  private String searchedTerm;

  public SearchState() {}

  public String getSearchedTerm() {
    return searchedTerm;
  }

  public String getError() {
    return error;
  }

  public String getRoomUid() {
    return roomUid;
  }

  public Boolean getIsError() {
    return isError;
  }

  public void setSearchedTerm(String searchedTerm) {
    this.searchedTerm = searchedTerm;
  }

  public void setRoomUid(String roomUid) {
    this.roomUid = roomUid;
  }

  public void setError(String error) {
    this.error = error;
  }

  public void setIsError(Boolean error) {
    isError = error;
  }
}
