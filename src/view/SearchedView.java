package view;

import entity.SearchResponse;
import interface_adapter.searched.SearchedState;
import interface_adapter.searched.SearchedViewModel;
import interface_adapter.switch_view.SwitchViewController;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class SearchedView implements PropertyChangeListener {

  public static final String viewName = "search results";

  private final SearchedViewModel searchedViewModel;

  private JButton backButton;

  private final JPanel returnedMessagesPanel;
  private JPanel contentPane;
  private JPanel rawPanel;

  private JPanel paneInternals;

  public SearchedView(
      SearchedViewModel searchedViewModel, SwitchViewController switchViewController) {
    returnedMessagesPanel = new JPanel();
    returnedMessagesPanel.setLayout(new BoxLayout(returnedMessagesPanel, BoxLayout.Y_AXIS));
    this.searchedViewModel = searchedViewModel;
    this.searchedViewModel.addPropertyChangeListener(this);

    rawPanel.setLayout(new BoxLayout(rawPanel, BoxLayout.Y_AXIS));

    paneInternals = new JPanel();
    paneInternals.setLayout(new BoxLayout(paneInternals, BoxLayout.Y_AXIS));

    JScrollPane scrollPane = new JScrollPane(paneInternals);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    scrollPane.setPreferredSize(new Dimension(300, 300));

    rawPanel.add(scrollPane);

    for (SearchResponse response : searchedViewModel.getState().getResponses().getResponses()) {
      JPanel eachMessagePanel = new JPanel();
      eachMessagePanel.setLayout(new BorderLayout());
      JLabel label =
          new JLabel(
              response.getTime().toString()
                  + ", "
                  + response.getAuthorName()
                  + ", "
                  + response.getRoomName()
                  + ": ");
      returnedMessagesPanel.add(label);
      JTextPane textPane = new JTextPane();
      textPane.setText(response.getFullText());
      StyledDocument doc = textPane.getStyledDocument();
      Style style = textPane.addStyle("HighlightStyle", null);
      StyleConstants.setBackground(style, Color.YELLOW);

      String openTag = "<em>";
      String closeTag = "</em>";

      try {
        String text = doc.getText(0, doc.getLength());
        int startIndex = 0;

        while ((startIndex = text.indexOf(openTag, startIndex)) != -1) {
          int endIndex = text.indexOf(closeTag, startIndex + openTag.length());

          if (endIndex != -1) {
            doc.setCharacterAttributes(
                startIndex, endIndex - startIndex + closeTag.length(), style, false);
            doc.remove(endIndex, closeTag.length());
            doc.remove(startIndex, openTag.length());
            text = doc.getText(0, doc.getLength());
          } else {
            break;
          }
        }
      } catch (BadLocationException e) {
        e.printStackTrace();
      }
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
