package ru.practicum;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class StatsClientImpl implements StatsClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;
    @Value("${stats-client.uri}")
    private final String uri;

    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        try {
            String query;
            if (uris.isEmpty()) {
                query = "?start=" + start + "&end=" + end + "&unique=" + unique;
            } else {
                query = "?start=" + start + "&end=" + end + "&unique=" + unique + "&uris=" + String.join(",", uris);
            }
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(uri + "/stats" + query))
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .build();
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            if (HttpStatus.valueOf(response.statusCode()).is2xxSuccessful()) {
                log.info("Успешно получена статистика по запросу: start: {}, end: {}, uris: {}, unique: {}",
                        start, end, uris, unique);
                return objectMapper.readValue(response.body(), new TypeReference<>() {
                });
            }
        } catch (Exception e) {
            log.error("ERROR не удалось получить статистику по запросу: start: {}, end: {}, uris: {}, unique: {}",
                    start, end, uris, unique);
        }
        return Collections.emptyList();
    }

    public void createHit(EndpointHit endpointHit) {
        if (endpointHit == null) {
            log.info("ERROR передан пустой endpointHit");
            return;
        }
        try {
            HttpRequest.BodyPublisher bodyPublisher = HttpRequest.BodyPublishers
                    .ofString(objectMapper.writeValueAsString(endpointHit));
            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(uri + "/hit"))
                    .POST(bodyPublisher)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .header(HttpHeaders.ACCEPT, "application/json")
                    .build();
            httpClient.send(httpRequest, HttpResponse.BodyHandlers.discarding());
            log.info("Успешно добавлена статистика: endpointHit: {}", endpointHit);
        } catch (Exception e) {
            log.error("ERROR не удалось добавить статистику: {}", endpointHit);
        }
    }
}