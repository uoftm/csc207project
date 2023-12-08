package entities.search;

import java.util.List;

public class SearchResponseDisplay {
  private final String time;
  private final String userName;
  private final String roomName;
  private final List<SearchIndices> highlightIndices;
  private final String message;

  public SearchResponseDisplay(
      String time,
      String userName,
      String roomName,
      List<SearchIndices> highlightIndices,
      String message) {
    this.time = time;
    this.userName = userName;
    this.roomName = roomName;
    this.highlightIndices = highlightIndices;
    this.message = message;
  }

  public String getTime() {
    return time;
  }

  public String getUserName() {
    return userName;
  }

  public String getRoomName() {
    return roomName;
  }

  public List<SearchIndices> getHighlightIndices() {
    return highlightIndices;
  }

  public String getMessage() {
    return message;
  }
}
