package use_case.search;

import entity.SearchChatMessage;
import entity.SearchReponseArray;
import entity.SearchRequest;

public interface SearchDataAccessInterface {

  public SearchReponseArray getData(SearchRequest request);

  public void saveData(SearchChatMessage message);
}
