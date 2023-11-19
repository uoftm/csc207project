package data_access;

import entity.Message;
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
    String stringified = new JSONObject().put("content", message.content).toString();

    Request request =
        new Request.Builder()
            .url(
                "https://csc207-830a5-default-rtdb.firebaseio.com/messages/"
                    + new Date().getTime()
                    + ".json")
            .method("POST", RequestBody.create(stringified, MediaType.get("application/json")))
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
            .url("https://csc207-830a5-default-rtdb.firebaseio.com/messages.json")
            .method("GET", null)
            .build();

    try {
      JSONObject response = new JSONObject(client.newCall(request).execute().body().string());
      var out = new ArrayList<Message>();
      var x = response.keys();
      while (x.hasNext()) {
        var key = x.next();
        var value = response.getJSONObject(key).getString("content");
        var time = Instant.ofEpochMilli(Long.parseLong(key));
        out.add(new Message(time, value));
      }
      out.sort(Comparator.comparing(a -> a.timestamp));

      return out;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
