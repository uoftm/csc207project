package entities.search;

import java.time.Instant;
import java.util.List;

public record SearchResponse(
    String fullText,
    String authUid,
    Instant time,
    String roomUid,
    List<SearchIndicies> highlightIndices) {}
