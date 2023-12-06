package entities.search;

import java.util.List;

public record SearchResponseDisplay(
    String time,
    String userName,
    String roomName,
    List<SearchIndicies> highlightIndices,
    String message) {}
