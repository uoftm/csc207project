package data_access;

import entities.rooms.Message;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.json.JSONObject;
import use_case.chat.ChatMessageDataAccessInterface;

public class FirebaseMessageDataAccessObject implements ChatMessageDataAccessInterface {
  private final OkHttpClient client;

  public FirebaseMessageDataAccessObject(OkHttpClient client) {
    this.client = client;
  }

  /**
   * Saves a message to the Firebase database.
   *
   * @param message the message to be saved
   */
  public void save(Message message) {
    // Stringify message
    String stringified =
        new JSONObject()
            .put("content", message.getContent())
            .put("author", message.getAuthorId())
            .toString();

    Request request =
        new Request.Builder()
            .url(Constants.FIREBASE_URL + "messages/" + new Date().getTime() + ".json")
            .method("PUT", RequestBody.create(stringified, MediaType.get("application/json")))
            .build();

    try {
      client.newCall(request).execute();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Retrieves all messages from the Firebase database.
   *
   * @return a list of Message objects containing all the messages
   */
  public List<Message> getAllMessages() {
    Request request =
        new Request.Builder()
            .url(Constants.FIREBASE_URL + "messages.json")
            .method("GET", null)
            .build();

    try {
      var response = client.newCall(request).execute();
      var body = response.body();
      JSONObject jsonResponse = new JSONObject(body.string());
      var out = new ArrayList<Message>();
      var x = jsonResponse.keys();
      while (x.hasNext()) {
        try {
          var key = x.next();
          var time = Instant.ofEpochMilli(Long.parseLong(key));
          var messageObject = jsonResponse.getJSONObject(key);
          var content = messageObject.getString("content");
          var authorId = messageObject.getString("author");
          out.add(new Message(time, content, authorId));
        } catch (RuntimeException e) {
          System.out.println("Invalid message");
          System.out.println(e.getLocalizedMessage());
        }
      }
      out.sort(Comparator.comparing(a -> a.getTimestamp()));

      return out;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
