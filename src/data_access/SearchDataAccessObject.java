package data_access;

import entities.search.SearchChatMessage;
import entities.search.SearchReponseArray;
import entities.search.SearchRequest;
import entities.search.SearchResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.search.SearchDataAccessInterface;

public class SearchDataAccessObject implements SearchDataAccessInterface {

  private OkHttpClient client;

  public SearchDataAccessObject(OkHttpClient okHttpClient) {
    this.client = okHttpClient;
  }

  /**
   * Retrieves messages based on the provided search query.
   *
   * @param searchRequest The search request containing the room ID and query.
   * @return A SearchReponseArray object containing the elasticsearch list of matches
   */
  public SearchReponseArray getData(SearchRequest searchRequest) {

    String esUrl = Constants.esUrl;
    String apiKey = Constants.apiKey;

    String index = "search-chats";

    JSONObject roomIDQuery =
        new JSONObject().put("term", new JSONObject().put("roomID", searchRequest.getRoomID()));
    JSONObject messageQuery =
        new JSONObject()
            .put(
                "match",
                new JSONObject()
                    .put(
                        "message",
                        new JSONObject()
                            .put("query", searchRequest.getQueryRequest())
                            .put("fuzziness", "AUTO")));
    JSONObject rangeFilter =
        new JSONObject()
            .put("range", new JSONObject().put("time", new JSONObject().put("gte", "now-1y/y")));

    JSONObject boolQuery =
        new JSONObject()
            .put("must", new JSONArray().put(roomIDQuery).put(messageQuery))
            .put("filter", new JSONArray().put(rangeFilter));

    JSONObject highlight =
        new JSONObject().put("fields", new JSONObject().put("message", new JSONObject()));

    JSONObject query =
        new JSONObject()
            .put("query", new JSONObject().put("bool", boolQuery))
            .put("size", 5)
            .put("highlight", highlight);

    Request request =
        new Request.Builder()
            .url(esUrl + "/" + index + "/_search")
            .addHeader("Authorization", "ApiKey " + apiKey)
            .addHeader("Content-Type", "application/json")
            .post(RequestBody.create(query.toString(), MediaType.get("application/json")))
            .build();

    try (Response response = client.newCall(request).execute()) {

      JSONObject rootNode = new JSONObject(response.body().string());
      JSONArray hitsArray = rootNode.getJSONObject("hits").getJSONArray("hits");
      ArrayList<SearchResponse> searchResponses = new ArrayList<>();
      Pattern pattern = Pattern.compile("<em>(.*?)</em>");
      for (int i = 0; i < hitsArray.length(); i++) {
        JSONObject hit = hitsArray.getJSONObject(i);
        JSONObject source = hit.getJSONObject("_source");
        JSONArray highlightNode = hit.getJSONObject("highlight").optJSONArray("message");
        String finalHighlight;
        if (highlightNode != null) {
          ArrayList<String> object = new ArrayList<>();
          for (int j = 0; j < highlightNode.length(); j++) {
            object.add(highlightNode.getString(j));
          }
          String highlightedText = object.get(0);
          Matcher matcher = pattern.matcher(highlightedText);
          matcher.find();
          finalHighlight = matcher.group().substring(4, 8);
        } else {
          finalHighlight = "";
        }
        SearchResponse oneResponse =
            new SearchResponse(
                finalHighlight,
                source.getString("message"),
                // TODO: use an in-memory cache to query username here instead
                source.optString("author"),
                Instant.parse(source.getString("time")),
                source.getString("roomID"));
        searchResponses.add(oneResponse);
      }
      return new SearchReponseArray(searchResponses, false);
    } catch (Exception e) {
      return new SearchReponseArray(new ArrayList<>(), true);
    }
  }

  /**
   * Saves a message to the elasticsearch database.
   *
   * @param message the message to be saved
   */
  public void saveData(SearchChatMessage message) {
    String esUrl = Constants.esUrl;
    String apiKey = Constants.apiKey;

    JSONObject json =
        new JSONObject()
            .put("time", message.getTime())
            .put("roomID", message.getRoomID())
            .put("message", message.getMessage())
            .put("author", message.getAuthorId());

    // Define the JSON data for bulk ingestion
    String jsonPayload =
        "{ \"index\" : { \"_index\" : \"search-chats\" } }\n" + json.toString() + "\n";

    RequestBody requestBody = RequestBody.create(jsonPayload, MediaType.get("application/json"));

    Request request =
        new Request.Builder()
            .url(esUrl + "/_bulk?pretty&pipeline=ent-search-generic-ingestion")
            .addHeader("Authorization", "ApiKey " + apiKey)
            .post(requestBody)
            .build();

    try (Response response = client.newCall(request).execute()) {
      if (response.isSuccessful()) {
        System.out.println("Request was successful");
        System.out.println("Response code: " + response.code());
        System.out.println("Response body:\n" + response.body().string());
      } else {
        System.err.println("Request failed");
        System.err.println("Response code: " + response.code());
        System.err.println("Response body:\n" + response.body().string());
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
