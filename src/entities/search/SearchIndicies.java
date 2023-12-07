package entities.search;

public class SearchIndicies {

  private final Integer start;
  private final Integer end;

  public SearchIndicies(Integer start, Integer end) {
    this.start = start;
    this.end = end;
  }

  public Integer getStart() {
    return start;
  }

  public Integer getEnd() {
    return end;
  }
}
