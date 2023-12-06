package interface_adapter.logged_in;

import use_case.logged_in.LoggedInOutputBoundary;

public class LoggedInPresenter implements LoggedInOutputBoundary {
  private final LoggedInViewModel loggedInViewModel;

  public LoggedInPresenter(LoggedInViewModel loggedInViewModel) {
    this.loggedInViewModel = loggedInViewModel;
  }

  @Override
  public void prepareSuccessView(String username) {
    loggedInViewModel.setLoggedInUser(username);
  }

  @Override
  public void prepareFailView(String error) {
    loggedInViewModel.setLoggedInUser(error);
  }
}
