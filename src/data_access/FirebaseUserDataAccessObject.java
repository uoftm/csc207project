package data_access;

import entity.User;
import java.io.IOException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.json.JSONException;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class FirebaseUserDataAccessObject
    implements SignupUserDataAccessInterface, LoginUserDataAccessInterface {
  static OkHttpClient client = new OkHttpClient();

  @Override
  public User get(String username) {
    Request request =
        new Request.Builder()
            .url(Constants.FIREBASE_URL + "users/" + username + ".json")
            .method("GET", null)
            .build();

    try {
      client.newCall(request).execute();
      return null;
    } catch (IOException | JSONException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean existsByName(String identifier) {
    return false;
  }

  @Override
  public void save(User user) {}
}
