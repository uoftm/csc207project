package entities.search;

public class SearchIndicies {

  private Integer start;
  private Integer end;

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
