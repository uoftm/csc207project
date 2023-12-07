package view;

import static org.junit.Assert.*;

import app.SearchUseCaseFactory;
import app.SwitchViewUseCaseFactory;
import data_access.ElasticsearchDataAccessObject;
import interface_adapter.ViewManagerModel;
import interface_adapter.search.*;
import interface_adapter.searched.SearchedViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import java.time.Instant;
import javax.swing.*;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;

public class SearchTest {
  private final OkHttpClient okHttpClient = new OkHttpClient();

  ElasticsearchDataAccessObject searchDataAccessObject;

  SearchViewModel searchViewModel;

  SearchedViewModel searchedViewModel;
  ViewManagerModel viewManagerModel;
  SwitchViewController switchViewController;
  JPanel views;

  @Before
  public void setUp() {
    CardLayout cardLayout = new CardLayout();
    views = new JPanel(cardLayout);
    viewManagerModel = new ViewManagerModel();

    switchViewController = SwitchViewUseCaseFactory.create(viewManagerModel);
    searchDataAccessObject = new ElasticsearchDataAccessObject(okHttpClient);

    searchViewModel = new SearchViewModel();
    searchedViewModel = new SearchedViewModel();
  }

  @Test
  public void testSearchViewBack() {
    SearchController searchController =
        SearchUseCaseFactory.createSearchController(
            searchViewModel, searchDataAccessObject, viewManagerModel, searchedViewModel);
    SearchView searchview = new SearchView(searchController, searchViewModel, switchViewController);
    views.add(searchview.contentPane, "searched");
    JFrame jf = new JFrame();
    jf.setContentPane(searchview.contentPane);
    jf.pack();
    jf.setVisible(true);
    searchview.getBackButton().doClick();
    assertEquals("logged in", viewManagerModel.getActiveView());
  }

  @Test
  public void testSavaDataFromController() {
    SearchController searchController =
        SearchUseCaseFactory.createSearchController(
            searchViewModel, searchDataAccessObject, viewManagerModel, searchedViewModel);
    searchController.executeRecordData(Instant.now(), "dummyRoomUid-2-2", "Test message!", "abc");
  }

  @Test
  public void testSearchViewSearch() {
    SearchController searchController =
        SearchUseCaseFactory.createSearchController(
            searchViewModel, searchDataAccessObject, viewManagerModel, searchedViewModel);
    SearchView searchview = new SearchView(searchController, searchViewModel, switchViewController);
    views.add(searchview.contentPane, "search");
    JFrame jf = new JFrame();
    jf.setContentPane(searchview.contentPane);
    jf.pack();
    jf.setVisible(true);
    searchview.getBackButton().doClick();
    assertEquals("logged in", viewManagerModel.getActiveView());
    SearchState state = searchViewModel.getState();
    state.setRoomUid("dummyRoomUid-2-2");
    searchViewModel.setState(state);
    searchViewModel.addPropertyChangeListener(searchview);
    searchview.getSearchBoxText().setText("Test essage!");
    searchview.getSearchBoxText().setText("Test message!");
    searchview.getSearchButton().doClick();
    assertEquals("search results", viewManagerModel.getActiveView());
  }

  @Test
  public void testSearchedView() {
    SearchController searchController =
        SearchUseCaseFactory.createSearchController(
            searchViewModel, searchDataAccessObject, viewManagerModel, searchedViewModel);
    SearchedView searchedview = new SearchedView(searchedViewModel, switchViewController);
    views.add(searchedview.contentPane, "searched");
    JFrame jf = new JFrame();
    jf.setContentPane(searchedview.contentPane);
    jf.pack();
    jf.setVisible(true);
    searchController.executeSearchRequest("dummyRoomUid", "Test message!", "abc");
    assertEquals("search results", viewManagerModel.getActiveView());
    searchedview.getBackButton().doClick();
    assertEquals("search", viewManagerModel.getActiveView());
  }

  @Test
  public void testNoDataErrorFromController() {
    SearchController searchController =
        SearchUseCaseFactory.createSearchController(
            searchViewModel, searchDataAccessObject, viewManagerModel, searchedViewModel);
    SearchView searchview = new SearchView(searchController, searchViewModel, switchViewController);
    views.add(searchview.contentPane, "search");
    JFrame jf = new JFrame();
    jf.setContentPane(searchview.contentPane);
    jf.pack();
    jf.setVisible(true);
    searchController.executeSearchRequest("---", "---", "---");
    assertEquals(null, viewManagerModel.getActiveView());
  }
}
