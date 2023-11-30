package interface_adapter.rooms;

import use_case.rooms.RoomsInputBoundary;
import use_case.rooms.RoomsInputData;

public class RoomsController {
    final RoomsInputBoundary roomsUseCaseInteractor;

    public RoomsController(RoomsInputBoundary roomsUseCaseInteractor) {
        this.roomsUseCaseInteractor = roomsUseCaseInteractor;
    }

    public void execute(String name) {
        RoomsInputData settingsInputData = new RoomsInputData(name);
        // Example request to load messages
        roomsUseCaseInteractor.loadMessages(settingsInputData);
    }
}
