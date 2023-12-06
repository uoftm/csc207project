package use_case.logged_in;

public interface LoggedInOutputBoundary {
  void prepareSuccessView(String username);

  void prepareFailView(String error);
}
