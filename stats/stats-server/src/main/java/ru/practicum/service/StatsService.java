package ru.practicum.service;

import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;

import java.util.List;

public interface StatsService {
    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);

    EndpointHitDto createHit(EndpointHitDto endpointHit);
}

