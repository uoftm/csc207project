package data_access;

public class Constants {
  public static final String FIREBASE_URL = "https://csc207-830a5-default-rtdb.firebaseio.com/";
  public static String esUrl =
      "https://e56e32ed3da843c3ba35b67803bd19b4.us-central1.gcp.cloud.es.io:443";
  public static String apiKey = "";

  public static final String SIGNUP_URL =
      "https://identitytoolkit.googleapis.com/v1/accounts:signUp";

  public static final String USER_DATA_URL = FIREBASE_URL + "users/%s.json";
  public static final String DISPLAY_NAME_URL = FIREBASE_URL + "users/%s/displayName.json";
  public static final String ROOM_DATA_URL = FIREBASE_URL + "users/%s/rooms.json";
  public static final String SPECIFIC_ROOM_DATA_URL = FIREBASE_URL + "users/%s/rooms/%s.json";
  public static final String ROOM_URL = FIREBASE_URL + "rooms/%s.json";
  public static final String LOGIN_URL =
      "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword";

  public static final String ACCOUNT_LOOKUP_URL =
      "https://identitytoolkit.googleapis.com/v1/accounts:lookup";
  public static final String DELETE_USER_URL =
      "https://identitytoolkit.googleapis.com/v1/accounts:delete";
  public static final String FIREBASE_AUTH_ID = "AIzaSyBuJk14Gljk-chdN_9YVywSKnf38ttwUVg";
}
