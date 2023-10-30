package data_access;

import entity.User;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

import java.io.IOException;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

public class FirebaseDataAccessObject implements SignupUserDataAccessInterface, LoginUserDataAccessInterface {

    static OkHttpClient client = new OkHttpClient();

    @Override
    public User get(String username) {
        Request request = new Request.Builder().url("https://csc207-830a5-default-rtdb.firebaseio.com/users/" + username + ".json").build();
        try {
            Response response = client.newCall(request).execute();

            JSONObject json =  new JSONObject(response.body().string());
            // Initialize user here
        } catch (IOException | JSONException e) {
            throw new RuntimeException(String.valueOf(e));
        }
    }

    @Override
    public boolean existsByName(String identifier) {
        Request request = new Request.Builder().url("https://csc207-830a5-default-rtdb.firebaseio.com/users.json").build();
        try {
            Response response = client.newCall(request).execute();

            JSONObject json =  new JSONObject(response.body().string());
            return false;
        } catch (IOException | JSONException e) {
            throw new RuntimeException(String.valueOf(e));
        }
    }

    @Override
    public void save(User user) {
        MediaType mediaType = MediaType.parse("application/json");
        // TODO: Format user data
        RequestBody body = RequestBody.create(JSONObject.quote(message), mediaType);
        Request request = new Request.Builder().url("https://csc207-830a5-default-rtdb.firebaseio.com/messages/" + System.currentTimeMillis() + ".json").method("PUT", body).addHeader("Content-Type", "application/json").build();

        try {
            client.newCall(request).execute();
        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
