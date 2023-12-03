package data_access;

import entities.auth.User;
import entities.rooms.Message;
import entities.rooms.Room;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

import okhttp3.*;
import org.json.JSONObject;
import use_case.login.LoginUserDataAccessInterface;
import use_case.rooms.MessageDataAccessInterface;

public class FirebaseMessageDataAccessObject implements MessageDataAccessInterface {
  private final OkHttpClient client;

  public FirebaseMessageDataAccessObject(OkHttpClient client) {
    this.client = client;
  }

  @Override
  public void sendMessage(Room room, LoginUserDataAccessInterface userDAO, User user, Message message) {
    String idToken = userDAO.getAccessToken(user.getEmail(), user.getPassword());
    String messageJSON =
            new JSONObject()
                    .put("content", message.content)
                    .put("author", message.authorEmail)
                    .toString();

    String url = String.format(Constants.MESSAGES_URL, room.getUid(), new Date().getTime()) + "?auth=" + idToken;
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
