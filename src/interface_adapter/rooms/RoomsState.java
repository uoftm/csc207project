package interface_adapter.rooms;

import entity.DisplayUser;
import entity.Message;

import java.util.List;

public class RoomsState {
    private String roomUid;
    private String userUid;

    public RoomsState(RoomsState copy) {
        roomUid = copy.roomUid;
        userUid = copy.userUid;
    }

    public RoomsState() {}

    public String getUserUid() {
        return userUid;
    }
    public String getRoomUid() {
        return roomUid;
    }
}
