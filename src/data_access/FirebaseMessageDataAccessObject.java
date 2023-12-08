package data_access;

import entities.auth.User;
import entities.rooms.Room;
import java.io.IOException;
import java.util.*;
import okhttp3.*;
import org.json.JSONObject;
import use_case.rooms.LoggedInDataAccessInterface;
import use_case.rooms.MessageDataAccessInterface;

/**
 * The FirebaseMessageDataAccessObject class provides methods for saving a message to the Firebase
 * database.
 */
public class FirebaseMessageDataAccessObject implements MessageDataAccessInterface {
  private final OkHttpClient client;

  /**
   * The FirebaseMessageDataAccessObject class provides a method for initializing a new instance of
   * the class.
   *
   * @param client The OkHttpClient instance used for making HTTP requests.
   */
  public FirebaseMessageDataAccessObject(OkHttpClient client) {
    this.client = client;
  }

  @Override
  public void sendMessage(Room room, LoggedInDataAccessInterface userDAO, String messageBody) {
    String idToken = userDAO.getIdToken();
    User user = userDAO.getUser();
    String messageJSON =
        new JSONObject().put("contents", messageBody).put("author", user.getEmail()).toString();

    String url =
        String.format(Constants.MESSAGES_URL, room.getUid(), new Date().getTime())
            + "?auth="
            + idToken;
    Request request =
        new Request.Builder()
            .url(url)
            .method("PUT", RequestBody.create(messageJSON, MediaType.get("application/json")))
            .build();

    try {
      Response response = client.newCall(request).execute();
      if (!response.isSuccessful()) {
        throw new IOException();
      }
    } catch (IOException e) {
      throw new RuntimeException("Unable to send message. Please try again.");
    }
  }
}
