package data_access;

import entities.search.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.search.SearchDataAccessInterface;

public class SearchDataAccessObject implements SearchDataAccessInterface {

  private OkHttpClient client;

  public SearchDataAccessObject(OkHttpClient okHttpClient) {
    this.client = okHttpClient;
  }

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

    JSONObject boolQuery =
        new JSONObject().put("must", new JSONArray().put(roomIDQuery).put(messageQuery));

    JSONObject highlight =
        new JSONObject()
            .put(
                "fields",
                new JSONObject()
                    .put("message", new JSONObject())
                    .put("*", new JSONObject().put("number_of_fragments", 0)));

    JSONObject query =
        new JSONObject()
            .put("query", new JSONObject().put("bool", boolQuery))
            .put("size", 10)
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
      System.out.println(query.toString());
      System.out.println(rootNode.toString());
      JSONArray hitsArray = rootNode.getJSONObject("hits").getJSONArray("hits");
      ArrayList<SearchResponse> searchResponses = new ArrayList<>();
      for (int i = 0; i < hitsArray.length(); i++) {
        JSONObject hit = hitsArray.getJSONObject(i);
        JSONObject source = hit.getJSONObject("_source");
        JSONArray highlightNode = hit.getJSONObject("highlight").optJSONArray("message");
        String finalHighlight;
        if (highlightNode != null) {
          finalHighlight = highlightNode.getString(0);
        } else {
          finalHighlight = "";
        }

        String openTag = "<em>";
        String closeTag = "</em>";
        List<SearchIndicies> item = new ArrayList<>();
        String text = finalHighlight;
        int currentIndex = 0;
        while (text.indexOf(openTag, currentIndex) != -1) {
          int start = text.indexOf(openTag, currentIndex) + openTag.length();
          int end = text.indexOf(closeTag, start);

          if (end != -1) {
            item.add(
                new SearchIndicies(
                    start - (openTag.length() * item.size() + closeTag.length() * item.size()),
                    end
                        - (openTag.length() * (item.size() + 1)
                            + closeTag.length() * item.size())));
            currentIndex = end + closeTag.length();
          } else {
            break;
          }
        }
        String fulltext = finalHighlight.replaceAll("<em>|</em>", "");

        SearchResponse oneResponse =
            new SearchResponse(
                fulltext,
                // TODO: use an in-memory cache to query username here instead
                source.optString("author"),
                Instant.parse(source.getString("time")),
                source.getString("roomID"),
                item);
        searchResponses.add(oneResponse);
      }
      return new SearchReponseArray(searchResponses, false);
    } catch (Exception e) {
      System.out.println("Search failed");
      e.printStackTrace();
      return new SearchReponseArray(new ArrayList<>(), true);
    }
  }

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

    System.out.println(jsonPayload);
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
