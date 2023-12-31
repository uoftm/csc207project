package data_access;

import entities.auth.User;
import org.junit.Assert;
import org.junit.Test;
import use_case.rooms.LoggedInDataAccessInterface;

/**
 * This class is the test class for the InMemoryUserDataAccessObject class. It contains test methods
 * to verify the functionality of the InMemoryUserDataAccessObject class.
 */
public class InMemoryDAOTest extends DAOTest {
  @Test
  public void checkNotFoundErrors() {
    LoggedInDataAccessInterface inMemoryDAO = new InMemoryUserDataAccessObject();
    Assert.assertThrows(RuntimeException.class, inMemoryDAO::getUser);
    Assert.assertThrows(RuntimeException.class, inMemoryDAO::getIdToken);
  }

  @Test
  public void checkGetData() {
    LoggedInDataAccessInterface inMemoryDAO = new InMemoryUserDataAccessObject();
    User user = createDummyUser();
    String idToken = "dummy token";
    inMemoryDAO.setUser(user);
    inMemoryDAO.setIdToken(idToken);
    Assert.assertEquals(inMemoryDAO.getUser(), user);
    Assert.assertEquals(inMemoryDAO.getIdToken(), idToken);
  }
}
