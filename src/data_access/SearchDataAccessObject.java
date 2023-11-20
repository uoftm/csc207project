package data_access;

import entity.SearchChatMessage;
import entity.SearchRequest;
import okhttp3.*;
import use_case.search.SearchDataAccessInterface;

public class SearchDataAccessObject implements SearchDataAccessInterface {

  @Override
  public void getData(SearchRequest searchRequest) {

    // Replace these placeholders with your actual Elasticsearch URL and API key
    String esUrl = "";
    String apiKey = "";

    OkHttpClient client = new OkHttpClient();

    // Define the Elasticsearch index to query
    String index = "search-chats";

    // Define your Elasticsearch query (in this case, a match query for the "name" field)
    String query = "{ \"query\": { \"match\": { \"name\": \"1984\" } } }";

    Request request =
        new Request.Builder()
            .url(esUrl + "/" + index + "/_search")
            .addHeader("Authorization", "ApiKey " + apiKey)
            .addHeader("Content-Type", "application/json")
            .post(RequestBody.create(query, MediaType.get("application/json")))
            .build();

    try (Response response = client.newCall(request).execute()) {
      if (response.isSuccessful()) {
        searchRequest.changeQueryResponse(response.body().string());
      } else {
        searchRequest.changeQueryResponse("sorry, didn't find anything!");
      }
    } catch (Exception e) {
      searchRequest.changeQueryResponse("sorry, didn't find anything!");
    }
  }

  @Override
  public void saveData(SearchChatMessage message) {
    // Replace these placeholders with your actual Elasticsearch URL and API key
    String esUrl = "your_elasticsearch_url";
    String apiKey = "your_api_key";

    // Define the JSON data for bulk ingestion
    String jsonPayload =
        """
        { "index" : { "_index" : "search-chats" } }
        {"name": "Snow Crash", "author": "Neal Stephenson", "release_date": "1992-06-01", "page_count": 470, "_extract_binary_content": true, "_reduce_whitespace": true, "_run_ml_inference": false}
        { "index" : { "_index" : "search-chats" } }
        {"name": "Revelation Space", "author": "Alastair Reynolds", "release_date": "2000-03-15", "page_count": 585, "_extract_binary_content": true, "_reduce_whitespace": true, "_run_ml_inference": false}
        { "index" : { "_index" : "search-chats" } }
        {"name": "1984", "author": "George Orwell", "release_date": "1985-06-01", "page_count": 328, "_extract_binary_content": true, "_reduce_whitespace": true, "_run_ml_inference": false}
        { "index" : { "_index" : "search-chats" } }
        {"name": "Fahrenheit 451", "author": "Ray Bradbury", "release_date": "1953-10-15", "page_count": 227, "_extract_binary_content": true, "_reduce_whitespace": true, "_run_ml_inference": false}
        { "index" : { "_index" : "search-chats" } }
        {"name": "Brave New World", "author": "Aldous Huxley", "release_date": "1932-06-01", "page_count": 268, "_extract_binary_content": true, "_reduce_whitespace": true, "_run_ml_inference": false}
        { "index" : { "_index" : "search-chats" } }
        {"name": "The Handmaid's Tale", "author": "Margaret Atwood", "release_date": "1985-06-01", "page_count": 311, "_extract_binary_content": true, "_reduce_whitespace": true, "_run_ml_inference": false}
        """;

    OkHttpClient client = new OkHttpClient();

    RequestBody requestBody = RequestBody.create(jsonPayload, MediaType.get("application/json"));

    Request request =
        new Request.Builder()
            .url(esUrl + "/_bulk?pretty&pipeline=ent-search-generic-ingestion")
            .addHeader("Authorization", "ApiKey " + apiKey)
            .post(requestBody)
            .build();
  }
}
