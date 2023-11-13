package use_case.search;

import entity.SearchChatMessage;
import entity.SearchRequest;

public interface SearchDataAccessInterface {
    public String getData(SearchRequest request);

    public void saveData(SearchChatMessage message);


}
