import static org.junit.Assert.*;

import data_access.ElasticsearchDataAccessObject;
import entities.search.SearchChatMessage;
import entities.search.SearchRequest;
import java.time.Instant;
import okhttp3.OkHttpClient;
import org.junit.Test;

public class SearchTest {
  private final OkHttpClient okHttpClient = new OkHttpClient();
  private final ElasticsearchDataAccessObject searchDataAccessObject =
      new ElasticsearchDataAccessObject(okHttpClient);

  @Test
  public void saveData() {
    SearchChatMessage searchMessage =
        new SearchChatMessage(Instant.now(), "baz", "ya", "test-author-id");
    searchDataAccessObject.saveData(searchMessage);
  }

  @Test
  public void testGetData() {
    SearchRequest searchRequest = new SearchRequest("asdf", "1234");
    assertEquals(
        searchDataAccessObject.getData(searchRequest).getResponses().get(0).getFullText(), "asdf");
  }

  @Test
  public void testGetNoData() {
    SearchRequest searchRequest = new SearchRequest("a", "Bro");
    assertEquals(
            searchDataAccessObject.getData(searchRequest).getError(), "No search results found.");
  }


}
