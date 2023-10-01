import okhttp3.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Use OkHTTP to make a get request to https://csc207-830a5-default-rtdb.firebaseio.com/messages.json
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://csc207-830a5-default-rtdb.firebaseio.com/messages.json")
                .build();
        try {
            Response response = client.newCall(request).execute();
            System.out.println(response);
            JSONObject responseBody = new JSONObject(response.body().string());
            System.out.println(responseBody);
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Hello World!");
    }
}
