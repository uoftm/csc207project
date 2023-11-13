package data_access;

import entity.SearchChatMessage;
import entity.SearchRequest;
import use_case.search.SearchDataAccessInterface;

public class SearchDataAccessObject implements SearchDataAccessInterface {
    @Override
    public String getData(SearchRequest request) {
        return null;
    }

    @Override
    public void saveData(SearchChatMessage message) {

    }
}
