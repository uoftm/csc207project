package data_access;

import entities.Message;
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

  public void save(Message message) {
    // Stringify message
    String stringified =
        new JSONObject().put("content", message.content).put("author", message.authorId).toString();

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

  public List<Message> getAllMessages() {
    Request request =
        new Request.Builder()
            .url(Constants.FIREBASE_URL + "messages.json")
            .method("GET", null)
            .build();

    try {
      JSONObject response = new JSONObject(client.newCall(request).execute().body().string());
      var out = new ArrayList<Message>();
      var x = response.keys();
      while (x.hasNext()) {
        try {
          var key = x.next();
          var time = Instant.ofEpochMilli(Long.parseLong(key));
          var messageObject = response.getJSONObject(key);
          var content = messageObject.getString("content");
          var authorId = messageObject.getString("author");
          out.add(new Message(time, content, authorId));
        } catch (RuntimeException e) {
          System.out.println("Invalid message");
          System.out.println(e.getLocalizedMessage());
        }
      }
      out.sort(Comparator.comparing(a -> a.timestamp));

      return out;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
