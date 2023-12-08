package use_case.rooms;

/**
 * The interface for the output boundary of the LoadRooms use case. It defines methods for preparing
 * success and fail views.
 */
public interface LoadRoomsOutputBoundary {
  /**
   * Prepares the success view for displaying rooms.
   *
   * @param outputData The output data containing the list of rooms and any potential error message.
   */
  void prepareSuccessView(LoadRoomsOutputData outputData);

  /**
   * Prepares the fail view for displaying rooms.
   *
   * @param outputData The output data containing the list of rooms and any potential error message.
   */
  void prepareFailView(LoadRoomsOutputData outputData);
}
