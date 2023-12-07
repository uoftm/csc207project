package data_access;

import entities.search.*;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import okhttp3.*;
import org.json.JSONArray;
import org.json.JSONObject;
import use_case.search.SearchDataAccessInterface;

public class ElasticsearchDataAccessObject implements SearchDataAccessInterface {

  private final OkHttpClient client;

  public ElasticsearchDataAccessObject(OkHttpClient okHttpClient) {
    this.client = okHttpClient;
  }

  public SearchReponseArray getData(SearchRequest searchRequest) {

    String esUrl = Constants.ELASTICSEARCH_URL;
    String apiKey = Constants.ELASTICSEARCH_API_KEY;

    String index = "search-chats";

    JSONObject roomIDQuery =
        new JSONObject()
            .put("match_phrase", new JSONObject().put("roomID", searchRequest.getRoomUid()));
    JSONObject messageQuery =
        new JSONObject()
            .put(
                "match_phrase",
                new JSONObject()
                    .put(
                        "message",
                        new JSONObject()
                            .put("query", searchRequest.getQueryRequest())
                            .put("slop", 2)));

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
      JSONArray hitsArray = rootNode.getJSONObject("hits").getJSONArray("hits");
      if (hitsArray.isEmpty()) {
        return new SearchReponseArray(new ArrayList<>(), "No search results found.", true);
      } else {
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
            int start = text.indexOf(openTag, currentIndex);
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

          // The above while loops involving the open and close Tags gets the indices for the
          // strings
          // to be
          // highlighted; As in the strings surrounded by "<em>|</em>". It gets the indices for them
          // after
          // the correspondent tags are removed. We need to indices to highlight the part of the
          // text
          // that is matched by elastic search in SearchedView.
          source.getString("roomID");
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
        return new SearchReponseArray(searchResponses, null, false);
      }
    } catch (Exception e) {
      System.out.println("Search failed");
      e.printStackTrace();
      return new SearchReponseArray(
          new ArrayList<>(), "Failed to get search results from elastic search.", true);
    }
  }

  public SearchReponseArray saveData(SearchChatMessage message) {
    String esUrl = Constants.ELASTICSEARCH_URL;
    String apiKey = Constants.ELASTICSEARCH_API_KEY;

    JSONObject json =
        new JSONObject()
            .put("time", message.getTime())
            .put("roomID", message.getRoomUid())
            .put("message", message.getMessage())
            .put("author", message.getAuthorEmail());

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
      if (!response.isSuccessful()) {
        return new SearchReponseArray(new ArrayList<>(), "Failed to save current message", true);
      } else {
        return new SearchReponseArray(new ArrayList<>(), "Success!", false);
      }
    } catch (Exception e) {
      e.printStackTrace();
      return new SearchReponseArray(new ArrayList<>(), "Failed to save current message", true);
    }
  }
}
