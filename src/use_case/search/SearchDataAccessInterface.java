package use_case.search;

import entities.search.SearchChatMessage;
import entities.search.SearchReponseArray;
import entities.search.SearchRequest;

public interface SearchDataAccessInterface {

  SearchReponseArray getData(SearchRequest request);

  void saveData(SearchChatMessage message);
}
