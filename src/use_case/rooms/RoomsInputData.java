package use_case.rooms;

public class RoomsInputData {
  String roomUid;
  String userUid;
  String message; // Nullable field

  public RoomsInputData(String roomUid, String userUid, String message) {
    this.roomUid = roomUid;
    this.userUid = userUid;
    this.message = message;
  }

  String getRoomUid() {
    return roomUid;
  }

  String getUserUid() {
    return userUid;
  }

  String getMessage() {
    return message;
  }
}
