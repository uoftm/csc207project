package interface_adapter.search;

public class SearchState {

  private String highlightedText;

  private String fullText;

  private String roomID;
  private String userID;

  private Boolean hasError = false;

  private String searchedTerm;

  public SearchState(
      String highlightedText,
      String fullText,
      String roomID,
      Boolean hasError,
      String searchedTerm) {
    this.highlightedText = highlightedText;
    this.fullText = fullText;
    this.roomID = roomID;
    this.hasError = hasError;
    this.searchedTerm = searchedTerm;
  }

  public SearchState() {}

  public String getSearchedTerm() {
    return searchedTerm;
  }

  public String getFullText() {
    return fullText;
  }

  public String getHighlightedText() {
    return highlightedText;
  }

  public Boolean getHasError() {
    return hasError;
  }

  public String getRoomID() {
    return roomID;
  }

  public void setSearchedTerm(String searchedTerm) {
    this.searchedTerm = searchedTerm;
  }

  public void setHighlightedText(String highlightedText) {
    this.highlightedText = highlightedText;
  }

  public void setFullText(String fullText) {
    this.fullText = fullText;
  }

  public void setRoomID(String roomID) {
    this.roomID = roomID;
  }

  public void setHasError(Boolean hasError) {
    this.hasError = hasError;
  }

  public String getUserUid() {
    return userID;
  }

  public void setUserUid(String userID) {
    this.userID = userID;
  }
}
