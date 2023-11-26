package data_access;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import entity.SearchChatMessage;
import entity.SearchReponseArray;
import entity.SearchRequest;
import entity.SearchResponse;
import java.time.Instant;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.search.SearchDataAccessInterface;

public class SearchDataAccessObject implements SearchDataAccessInterface {

  @Override
  public SearchReponseArray getData(SearchRequest searchRequest) {

    String esUrl = Constants.esUrl;
    String apiKey = Constants.apiKey;

    OkHttpClient client = new OkHttpClient();

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

      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(response.body().string());
      JsonNode hitsArray = rootNode.path("hits").path("hits");
      ArrayList<SearchResponse> searchResponses = new ArrayList<>();
      Pattern pattern = Pattern.compile("<em>(.*?)</em>");
      for (JsonNode hit : hitsArray) {
        JsonNode source = hit.path("_source");
        JsonNode highlightNode = hit.path("highlight").path("message");
        String finalHighlight;
        if (!highlightNode.isMissingNode()) {
          ArrayList<String> object =
              new ObjectMapper().convertValue(highlightNode, ArrayList.class);
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
                source.path("message").asText(),
                Instant.parse(source.path("time").asText()),
                source.path("roomID").asText());
        searchResponses.add(oneResponse);
      }
      return new SearchReponseArray(searchResponses, false);
    } catch (Exception e) {
      return new SearchReponseArray(new ArrayList<>(), true);
    }
  }

  @Override
  public void saveData(SearchChatMessage message) {
    String esUrl = Constants.esUrl;
    String apiKey = Constants.apiKey;

    JSONObject json =
        new JSONObject()
            .put("time", message.getTime())
            .put("roomID", message.getRoomID())
            .put("message", message.getMessage());

    OkHttpClient client = new OkHttpClient();
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
