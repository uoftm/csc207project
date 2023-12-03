package interface_adapter.search;

public class SearchState {

  private String roomUid;
  private String userUid;

  private String error;

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

  public void setSearchedTerm(String searchedTerm) {
    this.searchedTerm = searchedTerm;
  }

  public void setRoomUid(String roomUid) {
    this.roomUid = roomUid;
  }

  public void setError(String error) {
    this.error = error;
  }

  public String getUserUid() {
    return userUid;
  }

  public void setUserUid(String userUid) {
    this.userUid = userUid;
  }
}
