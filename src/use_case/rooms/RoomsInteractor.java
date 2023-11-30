package use_case.rooms;

public class RoomsInteractor implements RoomsInputBoundary {
    final RoomsOutputBoundary roomsPresenter;

    public RoomsInteractor(
            RoomsOutputBoundary roomsOutputBoundary) {
        this.roomsPresenter = roomsOutputBoundary;
    }

    // TODO: Write more interactions like this
    @Override
    public void loadMessages(RoomsInputData roomsInputData) {
        String uid = roomsInputData.getRoomUid();

        // Make request with uid here
        // and get error (can be null) back
        String error = null;
        boolean useCaseFailed = error != null;
        
        RoomsOutputData roomsOutputData = new RoomsOutputData(uid, useCaseFailed, error);
        roomsPresenter.prepareSuccessView(roomsOutputData);
    }
}
