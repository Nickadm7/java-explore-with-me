package ru.practicum.stats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;

import ru.practicum.stats.service.StatsService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/stats")
    public List<ViewStats> getStats(@RequestParam String start,
                                    @RequestParam String end,
                                    @RequestParam List<String> uris,
                                    @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Get запрос к /stats параметры start: {}, end: {}, uris: {}, unique: {}", start, end, uris, unique);
        return statsService.getStats(start, end, uris, unique);
    }

    @PostMapping("/hit")
    public EndpointHit postHit(@RequestBody EndpointHit endpointHit) {
        log.info("Post запрос к /hit {}", endpointHit);
        return statsService.createHit(endpointHit);
    }
}