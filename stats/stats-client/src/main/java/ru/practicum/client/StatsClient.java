package ru.practicum.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStats;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class StatsClient extends BaseClient {
    private final static String NAME = "EWM-SERVICE";

    @Autowired
    public StatsClient(@Value("${stats-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build()
        );
    }

    public void createHit(HttpServletRequest httpServletRequest) {
        log.info("Получен запрос на сохранение статистики name: {}, URL: {}, Addr: {}, time: {}",
                NAME, httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr(), LocalDateTime.now());
        post(new EndpointHitDto(
                NAME,
                httpServletRequest.getRequestURI(),
                httpServletRequest.getRemoteAddr(),
                LocalDateTime.now()));
    }

    public Map<Long, Long> getHits(List<Long> eventIds) {
        log.info("Получен запрос на простотр статистики по eventIds: {}", eventIds);
        if (eventIds != null && !eventIds.isEmpty()) {
            log.info("Список id событий пуст: {}", eventIds);
            return new HashMap<>();
        }
        StringBuilder constructUrl = new StringBuilder();
        LocalDateTime start = LocalDateTime.now().minusYears(50).truncatedTo(ChronoUnit.SECONDS);
        LocalDateTime end = LocalDateTime.now().plusYears(50).truncatedTo(ChronoUnit.SECONDS);
        Iterator<Long> id = eventIds.iterator();
        constructUrl.append("/events/")
                .append(id.next());
        while (id.hasNext()) {
            constructUrl.append("/events/").append(id.next());
        }
        ResponseEntity<Object> objects = get("stats?start={start}&end={end}&uris={uris}&unique={unique}", Map.of(
                "start", start,
                "end", end,
                "uris", constructUrl.toString(),
                "unique", false
        ));
        ObjectMapper objectMapper = new ObjectMapper();
        List<ViewStats> viewStats = objectMapper.convertValue(objects.getBody(), new TypeReference<>() {
        });
        if (viewStats == null) {
            log.info("Список ViewStats пуст: {}", viewStats);
            return new HashMap<>();
        } else {
            log.info("Успешно получен список ViewStats: {}", viewStats);
            return viewStats.stream()
                    .collect(Collectors.toMap(
                            v -> Long.parseLong(v.getUri().split("/", 0)[2]),
                            ViewStats::getHits));
        }
    }
}