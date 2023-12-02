package use_case.rooms;

/**
 * A rust style generic Response sum type that can either be: - an error, with null val - a success,
 * with null error
 */
public class Response<T> {
  private String error;
  private T val;

  public Response(T val) {
    this.val = val;
  }

  public Response(String error) {
    this.error = error;
  }

  public boolean isError() {
    return error != null;
  }

  public String getError() {
    return this.error;
  }

  public T getVal() {
    return this.val;
  }
}
