package use_case.rooms;

public class RoomsInputData {
    String roomUid;
    String userUid;

    public RoomsInputData(String roomUid, String userUid) {
        this.roomUid = roomUid;
        this.userUid = userUid;
    }
    String getRoomUid() {
        return roomUid;
    }
    String getUserUid() {
        return userUid;
    }
}
