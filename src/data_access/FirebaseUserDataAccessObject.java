package data_access;

import entity.User;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class FirebaseUserDataAccessObject
    implements SignupUserDataAccessInterface, LoginUserDataAccessInterface {
  static OkHttpClient client = new OkHttpClient();

  @Override
  public User get(String email) {
    Request request =
        new Request.Builder()
            .url(
                Constants.FIREBASE_URL
                    + "users/"
                    + email
                    + ".json") // TODO: Ensure this successfully searches by email
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
  } // TODO: Ensure this successfully searches by email

  @Override
  public Optional<String> save(User user) {
    Map<String, String> jsonMap = new HashMap<>();
    jsonMap.put("email", user.getEmail());
    jsonMap.put("password", user.getPassword());
    JSONObject jsonBody = new JSONObject(jsonMap);
    System.out.println(jsonBody.toString()); // TODO: Get rid of this

    HttpUrl.Builder urlBuilder = HttpUrl.parse(Constants.SIGNUP_URL).newBuilder();
    urlBuilder.addQueryParameter("key", Constants.FIREBASE_AUTH_ID);

    RequestBody body = RequestBody.create(MediaType.parse("application/json"), jsonBody.toString());

    Request request = new Request.Builder().url(urlBuilder.build().toString()).post(body).build();
    try {
      Response response = client.newCall(request).execute();
      if (response.code() == 200) {
        // Success
        return Optional.empty();
      } else {
        JSONObject jsonResponse = new JSONObject(response.body().string());
        System.out.println("Response body " + jsonResponse);
        JSONArray errors = jsonResponse.getJSONObject("error").getJSONArray("errors");
        String firstErrorMessage = errors.getJSONObject(0).getString("message");
        return Optional.of(firstErrorMessage);
      }
    } catch (IOException | JSONException e) {
      return Optional.of("Unexpected error signing up. Please try again.");
    }
  }
}
