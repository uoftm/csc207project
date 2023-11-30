package use_case.rooms;

import use_case.signup.SignupUserDataAccessInterface;

public class RoomsInteractor implements RoomsInputBoundary {
    final RoomsOutputBoundary roomsPresenter;
    final RoomsDataAccessInterface roomsDataAccessObject;

    public RoomsInteractor(
            RoomsDataAccessInterface roomsDataAccessObject,
            RoomsOutputBoundary roomsOutputBoundary) {
        this.roomsPresenter = roomsOutputBoundary;
        this.roomsDataAccessObject = roomsDataAccessObject;
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
