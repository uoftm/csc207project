package use_case.search;

import entities.SearchChatMessage;
import entities.SearchReponseArray;
import entities.SearchRequest;

public interface SearchDataAccessInterface {

  public SearchReponseArray getData(SearchRequest request);

  public void saveData(SearchChatMessage message);
}
