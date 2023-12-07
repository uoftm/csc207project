package data_access;

import entities.auth.User;
import okhttp3.OkHttpClient;
import org.junit.Assert;
import org.junit.Test;
import use_case.settings.DeleteUserDataAccessInterface;
import use_case.signup.SignupUserDataAccessInterface;

public class SignupDAOTest extends DAOTest {
  // Currently, the tests in this class cover the signup portion of the Firebase User DAO code
  // However, they don't clean up after themselves.
  // TODO: Write a delete user function that scrubs all user data
  //  (e.g. display name, membership list of room, deletes account)
  @Test
  public void testSaveUser() {
    OkHttpClient client = new OkHttpClient();
    FirebaseUserDataAccessObject dao = new FirebaseUserDataAccessObject(client);
    User testUser = createDummyUser();

    // Sign up user
    SignupUserDataAccessInterface signupDao = dao;
    signupDao.save(testUser);

    // Confirm the user has the details that were specified on signup
    String idToken = dao.getAccessToken(testUser.getEmail(), testUser.getPassword());
    User retrievedUser = dao.getUser(idToken, testUser.getEmail(), testUser.getPassword());
    Assert.assertEquals(retrievedUser.getEmail().toLowerCase(), testUser.getEmail().toLowerCase());
    Assert.assertEquals(retrievedUser.getName(), testUser.getName());
    Assert.assertEquals(retrievedUser.getPassword(), testUser.getPassword());

    // Delete user
    DeleteUserDataAccessInterface deleteDao = dao;
    deleteDao.deleteUser(idToken, retrievedUser);

    // Confirm user deletion
    Assert.assertThrows(
        RuntimeException.class,
        () -> dao.getUser(idToken, testUser.getEmail(), testUser.getPassword()));
  }
}
