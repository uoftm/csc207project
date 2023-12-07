package data_access;

import static org.junit.Assert.assertEquals;

import entities.search.SearchChatMessage;
import entities.search.SearchRequest;
import java.time.Instant;
import okhttp3.OkHttpClient;
import org.junit.Test;

public class SearchDAOTest {

  private final OkHttpClient okHttpClient = new OkHttpClient();

  final ElasticsearchDataAccessObject searchDataAccessObject =
      new ElasticsearchDataAccessObject(okHttpClient);

  @Test
  public void saveData() {
    SearchChatMessage searchMessage =
        new SearchChatMessage(Instant.now(), "-", "yaaaaa", "test-author-id");
    searchDataAccessObject.saveData(searchMessage);
  }

  @Test
  public void testGetData() {
    SearchRequest searchRequest = new SearchRequest("Test message!", "dummyRoomUid");
    assertEquals(
        searchDataAccessObject.getData(searchRequest).getResponses().get(0).getFullText(),
        "Test message!");
  }

  @Test
  public void testNoDataErrorFromDataAccess() {
    SearchRequest searchRequest = new SearchRequest("a", "Bro");
    assertEquals(
        searchDataAccessObject.getData(searchRequest).getError(), "No search results found.");
  }
}
