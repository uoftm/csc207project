import data_access.SearchDataAccessObject;
import entity.SearchRequest;
import org.junit.Test;

import java.time.Instant;

import static org.junit.Assert.*;



public class SearchTest {
    private SearchDataAccessObject searchDataAccessObject;

    @Test
    public void testGetData() {
        SearchDataAccessObject searchDataAccessObject = new SearchDataAccessObject();
        SearchRequest searchRequest = new SearchRequest("dog", Instant.now(), "2");
        assertEquals(searchDataAccessObject.getData(searchRequest).getResponses().getFirst().getFullText(), "I love dogs" );
    }



}
