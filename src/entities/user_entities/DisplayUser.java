package entities.user_entities;

public class DisplayUser implements AbstractUser {
  private final String uid;
  private final String name;

  public DisplayUser(String uid, String name) {
    this.uid = uid;
    this.name = name;
  }

  public String getUid() {
    return uid;
  }

  public String getName() {
    return name;
  }
}
