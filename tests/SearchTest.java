import static org.junit.Assert.*;

import data_access.SearchDataAccessObject;
import entities.search.SearchChatMessage;
import entities.search.SearchRequest;
import java.time.Instant;
import okhttp3.OkHttpClient;
import org.junit.Test;

public class SearchTest {
  private final OkHttpClient okHttpClient = new OkHttpClient();
  private final SearchDataAccessObject searchDataAccessObject =
      new SearchDataAccessObject(okHttpClient);

  @Test
  public void saveData() {
    SearchChatMessage searchMessage =
        new SearchChatMessage(Instant.now(), "test-room-id", "test-message", "test-author-id");
    searchDataAccessObject.saveData(searchMessage);
  }

  @Test
  public void testGetData() {
    SearchRequest searchRequest = new SearchRequest("dog", Instant.now(), "2");
    assertEquals(
        searchDataAccessObject.getData(searchRequest).getResponses().getFirst().getFullText(),
        "I love dogs");
  }
}
