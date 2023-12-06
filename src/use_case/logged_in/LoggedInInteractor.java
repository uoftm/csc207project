package use_case.logged_in;

import use_case.rooms.LoggedInDataAccessInterface;

public class LoggedInInteractor implements LoggedInInputBoundary {
  private final LoggedInDataAccessInterface inMemoryDAO;
  private final LoggedInOutputBoundary loggedInOutputBoundary;

  public LoggedInInteractor(
      LoggedInDataAccessInterface inMemoryDAO, LoggedInOutputBoundary loggedInOutputBoundary) {
    this.inMemoryDAO = inMemoryDAO;
    this.loggedInOutputBoundary = loggedInOutputBoundary;
  }

  @Override
  public void execute() {
    try {
      String username = inMemoryDAO.getUser().getName();
      loggedInOutputBoundary.prepareSuccessView(username);
    } catch (RuntimeException e) {
      loggedInOutputBoundary.prepareFailView("null");
    }
  }
}
