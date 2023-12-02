package use_case.rooms;

public class Response<T> {
  private String error; // Nullable field
  private T val;

  public Response(T val, String error) {
    this.val = val;
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
