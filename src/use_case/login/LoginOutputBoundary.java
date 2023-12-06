package use_case.login;

public interface LoginOutputBoundary {
  void prepareSuccessView();

  void prepareFailView(String error);
}
