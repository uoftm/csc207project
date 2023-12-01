package interface_adapter.rooms;

import use_case.rooms.RoomsOutputBoundary;
import use_case.rooms.RoomsOutputData;

public class RoomsPresenter implements RoomsOutputBoundary {
  private final RoomsViewModel roomsViewModel;

  public RoomsPresenter(RoomsViewModel roomsViewModel) {
    this.roomsViewModel = roomsViewModel;
  }

  @Override
  public void prepareSuccessView(RoomsOutputData response) {
    // On success, switch back to the same view for now
    System.out.println("success");
  }

  @Override
  public void prepareFailView(RoomsOutputData response) {
    // On error, switch back to the same view for now
    System.out.println(response.getError());
  }
}
