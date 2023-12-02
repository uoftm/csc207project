package interface_adapter.searched;

import entities.search.SearchResponseThing;
import java.util.ArrayList;

public class SearchedState {

  private ArrayList<SearchResponseThing> processedData;

  public SearchedState(ArrayList<SearchResponseThing> processedData) {
    this.processedData = processedData;
  }

  public SearchedState() {}

  public void setResponses(ArrayList<SearchResponseThing> processedData) {
    this.processedData = processedData;
  }

  public ArrayList<SearchResponseThing> getResponses() {
    return processedData;
  }
}
