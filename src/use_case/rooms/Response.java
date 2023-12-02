package use_case.rooms;

public class Response<T> {
  private String error;
  private T val;

  public Response(T val) {
    this.val = val;
  }

  public boolean isError() {
    return error != null;
  }

  public String getError() {
    return this.error;
  }

  public void setError(String error) {
    this.error = error;
  }

  public T getVal() {
    return this.val;
  }
}
