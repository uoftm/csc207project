package interface_adapter.rooms;

import entity.Message;
import use_case.rooms.RoomsOutputBoundary;
import use_case.rooms.RoomsOutputData;

import java.util.List;

public class RoomsPresenter implements RoomsOutputBoundary {

    private final RoomsViewModel roomsViewModel;

    public RoomsPresenter(RoomsViewModel roomsViewModel) {
        this.roomsViewModel = roomsViewModel;
    }

    @Override
    public void prepareSuccessView(RoomsOutputData response) {
        // On success, switch back to the same view for now
        System.out.println("success");
        System.out.println(response.getName());
    }

    @Override
    public void prepareFailView(RoomsOutputData response) {
        // On error, switch back to the same view for now
        System.out.println(response.getError());
    }
}
