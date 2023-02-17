package ru.practicum.stats.service;

import ru.practicum.stats.dto.ViewStats;

import java.util.List;

public interface StatsService {
    List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique);
}
