package data_access;

/**
 * This class contains all the constants used in the data access layer.
 *
 * <p>In particular, it contains our API endpoints and API keys.
 */
public class Constants {
  /**
   * The URL of the Firebase Realtime Database used in the application.
   *
   * <p>This constant holds the base URL for accessing the Firebase Realtime Database. It is used
   * for making API calls to read and write and read messages, username, and room info
   */
  public static final String FIREBASE_URL = "https://csc207-830a5-default-rtdb.firebaseio.com/";

  /**
   * The URL of the Elasticsearch instance used in the application.
   *
   * <p>This constant holds the URL of the Elasticsearch instance that is used for performing
   * searches and storing data. It is used in the data access layer to connect to the Elasticsearch
   * cluster.
   */
  public static final String ELASTICSEARCH_URL =
      "https://e56e32ed3da843c3ba35b67803bd19b4.us-central1.gcp.cloud.es.io:443";

  /**
   * The API key used for accessing the Elasticsearch instance.
   *
   * <p>If this key is missing, search may show empty messages without errors because of how
   * Elasticsearch handles unauthorized requests.
   */
  public static final String ELASTICSEARCH_API_KEY =
      "WjVmbl80c0JVOG1WUG5tNTFZREQ6SEFOVnU3QnlSczZud2FfMlk4RUxLdw==";

  /** The URL for user signup. This URL is used to create a new user account. */
  public static final String SIGNUP_URL =
      "https://identitytoolkit.googleapis.com/v1/accounts:signUp";

  /**
   * The URL pattern for accessing our custom user data on the Firebase database. The URL is
   * constructed by inserting the user's Base64 email using String.format.
   *
   * <p>Example: https://csc207-830a5-default-rtdb.firebaseio.com/users/aGVsbG9AaGVsbG8uY29t.json
   */
  public static final String USER_DATA_URL = FIREBASE_URL + "users/%s.json";

  /**
   * The URL pattern for accessing the display name of a user on the Firebase database. The URL is
   * constructed by inserting the user's Base64 email using String.format.
   *
   * <p>Example:
   * https://csc207-830a5-default-rtdb.firebaseio.com/users/aGVsbG9AaGVsbG8uY29t/displayName.json
   */
  public static final String DISPLAY_NAME_URL = FIREBASE_URL + "users/%s/displayName.json";

  /**
   * The URL template for retrieving room data from the Firebase Realtime Database. It should be
   * formatted using the user's email.
   */
  public static final String ROOM_DATA_URL = FIREBASE_URL + "users/%s/rooms.json";

  public static final String SPECIFIC_ROOM_DATA_URL = FIREBASE_URL + "users/%s/rooms/%s.json";

  /**
   * The constant variable ROOM_URL represents the URL format for retrieving room data from
   * Firebase.
   *
   * <p>String roomUrl = String.format(ROOM_URL, roomId);
   */
  public static final String ROOM_URL = FIREBASE_URL + "rooms/%s.json";

  /** The URL pattern for retrieving room users in the Firebase database. */
  public static final String ROOM_USERS_URL = FIREBASE_URL + "rooms/%s/users/%s.json";

  /** The URL template for retrieving the name of a room from Firebase. */
  public static final String ROOM_NAME_URL = FIREBASE_URL + "rooms/%s/name.json";

  /** */
  public static final String MESSAGES_URL = FIREBASE_URL + "rooms/%s/messages/%s.json";

  /**
   * The URL for the login endpoint. This URL is used to authenticate a user with their email and
   * password.
   *
   * @see <a href="https://firebase.google.com/docs/reference/rest/auth/">Google Identity Toolkit
   *     API</a>
   */
  public static final String LOGIN_URL =
      "https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword";

  /**
   * URL for looking up user accounts using the Identity Toolkit API.
   *
   * @see <a href="https://firebase.google.com/docs/reference/rest/auth/">Google Identity Toolkit
   *     API</a>
   */
  public static final String ACCOUNT_LOOKUP_URL =
      "https://identitytoolkit.googleapis.com/v1/accounts:lookup";

  /**
   * The URL for deleting a user account. This URL is used to send a request to the Google Identity
   * Toolkit API in order to delete a user account from the Firebase project.
   *
   * @see <a href="https://firebase.google.com/docs/reference/rest/auth/">Google Identity Toolkit
   *     API</a>
   */
  public static final String DELETE_USER_URL =
      "https://identitytoolkit.googleapis.com/v1/accounts:delete";

  /** The (Secret) Firebase token used for authenticating with Firebase services. */
  public static final String FIREBASE_AUTH_ID = "AIzaSyBuJk14Gljk-chdN_9YVywSKnf38ttwUVg";
}
