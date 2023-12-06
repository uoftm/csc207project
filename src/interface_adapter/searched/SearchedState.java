package interface_adapter.searched;

import entities.search.SearchResponseDisplay;
import java.util.ArrayList;

public class SearchedState {

  private ArrayList<SearchResponseDisplay> processedData;

  public SearchedState() {}
  public void setResponses(ArrayList<SearchResponseDisplay> processedData) {
    this.processedData = processedData;
  }

  public ArrayList<SearchResponseDisplay> getResponses() {
    return processedData;
  }
}
