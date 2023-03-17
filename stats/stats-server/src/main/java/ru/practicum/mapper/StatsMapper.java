package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.model.Hit;

import java.time.format.DateTimeFormatter;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StatsMapper {
    private static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Hit endpointHitToHit(EndpointHitDto endpointHit) {
        return new Hit(
                null,
                endpointHit.getApp(),
                endpointHit.getUri(),
                endpointHit.getIp(),
                endpointHit.getTimestamp()
        );
    }

    public static EndpointHitDto hitToEndpointHit(Hit hit) {
        return new EndpointHitDto(
                hit.getApp(),
                hit.getUri(),
                hit.getIp(),
                hit.getTimestamp()
        );
    }
}