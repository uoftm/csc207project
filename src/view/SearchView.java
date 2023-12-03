package view;

import interface_adapter.search.SearchController;
import interface_adapter.search.SearchState;
import interface_adapter.search.SearchViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SearchView implements PropertyChangeListener {
  public static final String viewName = "search";

  private JButton search;

  private JTextField searchBoxText;
  public JPanel contentPane;
  private JButton backButton;

  private SearchViewModel searchViewModel;

  public SearchView(
      SearchController searchController,
      SearchViewModel searchviewModel,
      SwitchViewController switchViewController) {
    this.searchViewModel = searchviewModel;
    searchviewModel.addPropertyChangeListener(this);

    searchBoxText
        .getDocument()
        .addDocumentListener(
            new DocumentListener() {
              @Override
              public void insertUpdate(DocumentEvent e) {
                update();
              }

              @Override
              public void removeUpdate(DocumentEvent e) {
                update();
              }

              @Override
              public void changedUpdate(DocumentEvent e) {
                update();
              }

              protected void update() {
                String text = searchBoxText.getText();

                SearchState currentstate = searchviewModel.getState();

                currentstate.setSearchedTerm(text);
                searchViewModel.setState(currentstate);
              }
            });

    SearchState currentState = searchViewModel.getState();

    search.addActionListener(
        e -> {
          searchController.executeSearchRequest(
              searchViewModel.getState().getRoomUid(),
              currentState.getSearchedTerm(),
              searchViewModel.getState().getUserUid());
        });

    backButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(backButton)) {
            switchViewController.switchTo(LoggedInView.viewName);
          }
        });
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {
    SearchState state = (SearchState) evt.getNewValue();
    if (state.getIsError()) {
      JOptionPane.showMessageDialog(contentPane, state.getError());
    }
  }
}
