package ru.practicum.stats.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.EndpointHit;
import ru.practicum.ViewStats;
import ru.practicum.stats.exception.ValidationException;
import ru.practicum.stats.mapper.StatsMapper;
import ru.practicum.stats.model.Hit;
import ru.practicum.stats.repository.StatsRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatServiceImpl implements StatsService {
    private final StatsRepository statsRepository;
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public List<ViewStats> getStats(String start, String end, List<String> uris, Boolean unique) {
        List<ViewStats> hits;
        LocalDateTime startTime = LocalDateTime.parse(start, DateTimeFormatter.ofPattern(TIME_FORMAT));
        LocalDateTime endTime = LocalDateTime.parse(end, DateTimeFormatter.ofPattern(TIME_FORMAT));
        if (startTime.isAfter(endTime)) {
            throw new ValidationException("startTime позже endTime");
        }
        if (unique) {
            hits = statsRepository.findAllHitsUnique(startTime, endTime, uris);
        } else {
            hits = statsRepository.findAllHitsNoUnique(startTime, endTime, uris);
        }
        return hits;
    }

    @Override
    @Transactional
    public EndpointHit createHit(EndpointHit endpointHit) {
        if (endpointHit.getApp() == null || endpointHit.getApp().isEmpty()) {
            throw new ValidationException("App не должен быть null");
        }
        if (endpointHit.getUri() == null || endpointHit.getUri().isEmpty()) {
            throw new ValidationException("Uri не должен быть null");
        }
        if (endpointHit.getIp() == null || endpointHit.getIp().isEmpty()) {
            throw new ValidationException("Ip не должен быть null");
        }
        if (endpointHit.getTimestamp() == null || endpointHit.getTimestamp().isEmpty()) {
            throw new ValidationException("Timestamp не должен быть null");
        }
        Hit hit = StatsMapper.endpointHitToHit(endpointHit);
        statsRepository.save(hit);
        log.info("Сохранен просмотр: {} в базу данных", hit);
        return StatsMapper.hitToEndpointHit(hit);
    }
}