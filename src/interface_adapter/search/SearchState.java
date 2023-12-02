package interface_adapter.search;

import java.time.Instant;

public class SearchState {

  private String highlightedText;

  private String fullText;

  private Instant time;

  private String roomID;

  private Boolean hasError = false;

  private String searchedTerm;

  public SearchState(
      String highlightedText,
      String fullText,
      Instant time,
      String roomID,
      Boolean hasError,
      String searchedTerm) {
    this.highlightedText = highlightedText;
    this.fullText = fullText;
    this.time = time;
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

  public Instant getTime() {
    return time;
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

  public void setTime(Instant time) {
    this.time = time;
  }

  public void setRoomID(String roomID) {
    this.roomID = roomID;
  }

  public void setHasError(Boolean hasError) {
    this.hasError = hasError;
  }
}
