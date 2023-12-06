package interface_adapter.logged_in;

import use_case.logged_in.LoggedInInputBoundary;
import use_case.login.LoginInputBoundary;

public class LoggedInController {
    final LoggedInInputBoundary loggedInUseCaseInteractor;

    public LoggedInController(LoggedInInputBoundary loggedInUseCaseInteractor) {
        this.loggedInUseCaseInteractor = loggedInUseCaseInteractor;
    }

    public void execute() {
        loggedInUseCaseInteractor.execute();
    }
}
