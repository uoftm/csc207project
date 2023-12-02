package view;

import interface_adapter.searched.SearchedState;
import interface_adapter.searched.SearchedViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.text.*;

public class SearchedView implements PropertyChangeListener {

  public static final String viewName = "search results";

  private final SearchedViewModel searchedViewModel;

  private JButton backButton;
  public JPanel contentPane;
  private JPanel rawPanel;

  private JPanel paneInternals;

  public SearchedView(
      SearchedViewModel searchedViewModel, SwitchViewController switchViewController) {
    this.searchedViewModel = searchedViewModel;
    this.searchedViewModel.addPropertyChangeListener(this);

    rawPanel.setLayout(new BoxLayout(rawPanel, BoxLayout.Y_AXIS));

    paneInternals = new JPanel();
    paneInternals.setLayout(new BoxLayout(paneInternals, BoxLayout.Y_AXIS));

    JScrollPane scrollPane = new JScrollPane(paneInternals);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(300, 300));

    rawPanel.add(scrollPane);

    var highlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
    for (var response : searchedViewModel.getState().getResponses()) {
      JPanel eachMessagePanel = new JPanel();
      eachMessagePanel.setLayout(new BorderLayout());
      JLabel label = new JLabel(response.label);
      eachMessagePanel.add(label);
      JTextPane textPane = new JTextPane();
      textPane.setText(response.rawMessage);
      eachMessagePanel.add(textPane, BorderLayout.CENTER);

      paneInternals.add(eachMessagePanel);
    }

    backButton.addActionListener(
        evt -> {
          if (evt.getSource().equals(backButton)) {
            switchViewController.switchTo(SearchView.viewName);
          }
        });
  }

  @Override
  public void propertyChange(PropertyChangeEvent evt) {

    SearchedState state = (SearchedState) evt.getNewValue();
  }
}
