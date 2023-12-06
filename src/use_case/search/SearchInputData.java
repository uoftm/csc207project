package use_case.search;

import java.time.Instant;

public record SearchInputData(Instant time, String roomUid, String message, String authUid) {}
