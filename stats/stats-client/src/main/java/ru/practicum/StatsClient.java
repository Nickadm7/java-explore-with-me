package ru.practicum;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsClient {
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);

    void createHit(EndpointHit endpointHit);
}