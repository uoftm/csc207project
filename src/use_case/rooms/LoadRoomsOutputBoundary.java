package use_case.rooms;

public interface LoadRoomsOutputBoundary {
  void prepareSuccessView(LoadRoomsOutputData outputData);

  void prepareFailView(LoadRoomsOutputData outputData);
}
