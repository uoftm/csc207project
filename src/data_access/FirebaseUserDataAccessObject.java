package data_access;

import entity.User;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;

import entity.UserFactory;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.login.LoginUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class FirebaseUserDataAccessObject
    implements SignupUserDataAccessInterface, LoginUserDataAccessInterface {
  private final UserFactory userFactory;
  static OkHttpClient client = new OkHttpClient();

  public FirebaseUserDataAccessObject(UserFactory userFactory) {
    this.userFactory = userFactory;
  }

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
  public String save(User user) {
    String firebaseResponse = firebaseSignup(user.getEmail(), user.getPassword());
    return firebaseResponse;  // To deal with error handling in the interactors
  }

  public String firebaseSignup(String email, String password) {
    try {
      URL url = new URL("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=AIzaSyBuJk14Gljk-chdN_9YVywSKnf38ttwUVg");
      HttpURLConnection conn = (HttpURLConnection) url.openConnection();
      conn.setRequestMethod("POST");
      conn.setRequestProperty("Content-Type", "application/json; utf-8");
      conn.setRequestProperty("Accept", "application/json");
      conn.setDoOutput(true);

      JSONObject jsonInput = new JSONObject();
      jsonInput.put("email", email);
      jsonInput.put("password", password);
      jsonInput.put("returnSecureToken", true);

      try(OutputStream os = conn.getOutputStream()) {
        byte[] input = jsonInput.toString().getBytes("utf-8");
        os.write(input, 0, input.length);
      }

      int responseCode = conn.getResponseCode();

      InputStream inputStream;
      if (responseCode == HttpURLConnection.HTTP_OK) {
        inputStream = conn.getInputStream();
      } else {
        inputStream = conn.getErrorStream();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine;
        while ((responseLine = br.readLine()) != null) {
          response.append(responseLine.trim());
        }
        br.close();

        JSONObject jsonResponse = new JSONObject(response.toString());
        if (jsonResponse.has("error")) {
          String errorMessage = jsonResponse.getJSONObject("error").getString("message");
          return errorMessage; // Return the error message
        }
      }

      // handle success response

    } catch (Exception e) {
      e.printStackTrace();
      return "Signup failed: " + e.getMessage(); // Return error message for exceptions
    }
    return "Success";
  }
}
