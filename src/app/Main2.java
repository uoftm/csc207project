package app;

import java.io.IOException;
import java.util.Scanner;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

public class Main2 {
  static OkHttpClient client = new OkHttpClient();

  public static void main(String[] args) {
    Scanner myObj = new Scanner(System.in);
    System.out.print("Enter message > ");

    String message = myObj.nextLine();
    Main2.sendMessage(message);

    System.out.println("Messages: " + Main2.getMessages());

    System.out.println("Hello World!");
  }

  static void sendMessage(String message) {
    MediaType mediaType = MediaType.parse("application/json");
    RequestBody body = RequestBody.create(JSONObject.quote(message), mediaType);
    Request request =
        new Request.Builder()
            .url(
                "https://csc207-830a5-default-rtdb.firebaseio.com/messages/"
                    + System.currentTimeMillis()
                    + ".json")
            .method("PUT", body)
            .addHeader("Content-Type", "application/json")
            .build();

    try {
      client.newCall(request).execute();
    } catch (IOException | JSONException e) {
      throw new RuntimeException(e);
    }
  }

  static JSONObject getMessages() {
    Request request =
        new Request.Builder()
            .url("https://csc207-830a5-default-rtdb.firebaseio.com/messages.json")
            .build();
    try {
      Response response = client.newCall(request).execute();

      return new JSONObject(response.body().string());
    } catch (IOException | JSONException e) {
      throw new RuntimeException(e);
    }
  }
}
