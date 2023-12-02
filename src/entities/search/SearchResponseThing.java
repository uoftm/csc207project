package entities.search;

import java.util.List;

public class SearchResponseThing {
  public String label;
  public List<SearchIndicies> highlightIndices;
  public String rawMessage;

  public SearchResponseThing(
      String label, List<SearchIndicies> highlightIndices, String rawMessage) {
    this.label = label;
    this.highlightIndices = highlightIndices;
    this.rawMessage = rawMessage;
  }
}
