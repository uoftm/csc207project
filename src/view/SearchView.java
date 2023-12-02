package view;

import interface_adapter.rooms.RoomsViewModel;
import interface_adapter.search.SearchController;
import interface_adapter.search.SearchState;
import interface_adapter.search.SearchViewModel;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.time.Instant;
import javax.swing.*;
import use_case.chat.ChatUserDataAccessInterface;

public class SearchView implements PropertyChangeListener {
  public static final String viewName = "search";

  private JButton search;

  private JTextField searchBoxText;
  private JScrollPane contentPane;

  private SearchViewModel searchViewModel;

  public SearchView(
      SearchController searchController,
      SearchViewModel searchviewModel,
      RoomsViewModel roomsViewModel,
      ChatUserDataAccessInterface userDataAccessObject) {
    this.searchViewModel = searchviewModel;
    searchviewModel.addPropertyChangeListener(this);

    searchBoxText.addKeyListener(
        new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
            SearchState currentstate = searchviewModel.getState();
            String text = searchBoxText.getText() + e.getKeyChar();
            currentstate.setSearchedTerm(text);
            searchViewModel.setState(currentstate);
          }

          @Override
          public void keyPressed(KeyEvent e) {}

          @Override
          public void keyReleased(KeyEvent e) {}
        });

    SearchState currentState = searchViewModel.getState();

    search.addActionListener(
        e -> {
          searchController.executeSearchRequest(
              Instant.now(),
              roomsViewModel.getState().getRoomUid(),
              currentState.getSearchedTerm(),
              userDataAccessObject.get().getUid());
        });
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    SearchState state = (SearchState) evt.getNewValue();
    if (state.getHasError()) {
      JOptionPane.showMessageDialog(contentPane, "Sorry, I couldn't find any past messages!");
    }
  }
}
