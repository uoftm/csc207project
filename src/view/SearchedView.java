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

  private JButton backButton;
  public JPanel contentPane;
  private JPanel rawPanel;

  private final JPanel paneInternals;

  public SearchedView(
      SearchedViewModel searchedViewModel, SwitchViewController switchViewController) {
    searchedViewModel.addPropertyChangeListener(this);

    rawPanel.setLayout(new BoxLayout(rawPanel, BoxLayout.Y_AXIS));

    paneInternals = new JPanel();
    paneInternals.setLayout(new BoxLayout(paneInternals, BoxLayout.Y_AXIS));

    JScrollPane scrollPane = new JScrollPane(paneInternals);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(300, 300));

    rawPanel.add(scrollPane);

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

    var highlighter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);
    for (var response : state.getResponses()) {
      JPanel eachMessagePanel = new JPanel();
      eachMessagePanel.setLayout(new FlowLayout());
      JLabel labelOne = new JLabel(response.time());
      JLabel labelTwo = new JLabel(response.roomName());
      JLabel labelThree = new JLabel(response.userName());
      JTextPane textPane = new JTextPane();
      textPane.setText(response.message());
      textPane.setEditable(false);

      for (var index : response.highlightIndices()) {
        try {
          textPane.getHighlighter().addHighlight(index.getStart(), index.getEnd(), highlighter);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }

      eachMessagePanel.add(labelOne);
      eachMessagePanel.add(labelTwo);
      eachMessagePanel.add(labelThree);
      eachMessagePanel.add(textPane);

      paneInternals.add(eachMessagePanel);
    }

    SwingUtilities.invokeLater(
        () -> {
          paneInternals.revalidate();
          paneInternals.repaint();
        });
  }
}
