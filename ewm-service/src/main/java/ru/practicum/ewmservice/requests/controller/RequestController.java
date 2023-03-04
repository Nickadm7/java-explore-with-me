package ru.practicum.ewmservice.requests.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.requests.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.requests.service.RequestService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestService requestService;

    @GetMapping
    public List<ParticipationRequestDto> getAllRequestForCurrentUser(@PathVariable("userId") @Positive Long userId) {
        log.info("Get запрос к /users/{userId}/requests с параметрами userId: {}", userId);
        return requestService.getAllRequestForCurrentUser(userId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public ParticipationRequestDto create(@PathVariable("userId") @Positive Long userId,
                                          @RequestParam(name = "eventId") @Positive Long eventId) {
        log.info("Post запрос к /users/{userId}/requests с параметрами userId: {}, eventId: {}", userId, eventId);
        return requestService.createRequestForCurrentUser(userId, eventId);
    }

    @PatchMapping("/{requestId}/cancel")
    public ParticipationRequestDto cancel(@NotNull @PathVariable Long userId,
                                          @NotNull @PathVariable Long requestId) {
        log.info("Post запрос к /users/{userId}/requests с параметрами userId: {}, requestId: {}", userId, requestId);
        return requestService.cancelRequestForOwner(userId, requestId);
    }
}
