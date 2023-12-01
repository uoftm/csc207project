package use_case.search;

import entities.search.SearchChatMessage;
import entities.search.SearchReponseArray;
import entities.search.SearchRequest;

public interface SearchDataAccessInterface {

  public SearchReponseArray getData(SearchRequest request);

  public void saveData(SearchChatMessage message);
}
