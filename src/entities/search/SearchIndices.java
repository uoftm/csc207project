package entities.search;

/**
 * The SearchIndices class represents the start and end indices of a search highlight in a text.
 */
public class SearchIndices {
  private final Integer start;
  private final Integer end;

  /**
   * The SearchIndices class represents the start and end indices of a search highlight in a text.
   *
   * @param start The starting index of the search highlight.
   * @param end The ending index of the search highlight.
   */
  public SearchIndices(Integer start, Integer end) {
    this.start = start;
    this.end = end;
  }

  /**
   * @return The starting index of the search highlight.
   */
  public Integer getStart() {
    return start;
  }

  /**
   * @return The ending index of the search highlight.
   */
  public Integer getEnd() {
    return end;
  }
}
