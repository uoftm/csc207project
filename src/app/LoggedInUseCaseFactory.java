package app;

import interface_adapter.logged_in.LoggedInController;
import interface_adapter.logged_in.LoggedInPresenter;
import interface_adapter.logged_in.LoggedInViewModel;
import interface_adapter.switch_view.SwitchViewController;
import use_case.logged_in.LoggedInInputBoundary;
import use_case.logged_in.LoggedInOutputBoundary;
import use_case.logged_in.LoggedInInteractor;
import use_case.rooms.LoggedInDataAccessInterface;
import view.LoggedInView;
import view.RoomsView;

public class LoggedInUseCaseFactory {
    private LoggedInUseCaseFactory() {}

    public static LoggedInView create(LoggedInViewModel loggedInViewModel, LoggedInDataAccessInterface inMemoryDAO, RoomsView roomsView, SwitchViewController switchViewController) {
        LoggedInOutputBoundary loggedInPresenter = new LoggedInPresenter(loggedInViewModel);
        LoggedInInputBoundary loggedInInteractor = new LoggedInInteractor(inMemoryDAO, loggedInPresenter);
        LoggedInController loggedInController = new LoggedInController(loggedInInteractor);
        LoggedInView loggedInView =
                new LoggedInView(
                        loggedInViewModel, roomsView, loggedInController, switchViewController);
        return loggedInView;
    }
}
