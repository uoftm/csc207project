package data_access;

import entity.Message;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
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
      for (Iterator<String> it = x; it.hasNext(); ) {
        var key = it.next();
        var value = response.getJSONObject(key);
        out.add(new Message(value.getString("content")));
      }

      return out;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
