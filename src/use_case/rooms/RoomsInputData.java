package use_case.rooms;

public class RoomsInputData {

    String uid;

    public RoomsInputData(String uid) {
        this.uid = uid;
    }

    String getRoomUid() {
        return uid;
    }
}
