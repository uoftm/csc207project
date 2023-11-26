import static org.junit.Assert.*;

import data_access.SearchDataAccessObject;
import entity.SearchRequest;
import java.time.Instant;
import okhttp3.OkHttpClient;
import org.junit.Test;

public class SearchTest {
  private SearchDataAccessObject searchDataAccessObject;

  private final OkHttpClient okHttpClient = new OkHttpClient();

  @Test
  public void testGetData() {
    SearchDataAccessObject searchDataAccessObject = new SearchDataAccessObject(okHttpClient);
    SearchRequest searchRequest = new SearchRequest("dog", Instant.now(), "2");
    assertEquals(
        searchDataAccessObject.getData(searchRequest).getResponses().getFirst().getFullText(),
        "I love dogs");
  }
}
