package ru.practicum.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.EventRequestStatusUpdateRequest;
import ru.practicum.event.model.EventRequestStatusUpdateResult;
import ru.practicum.event.service.EventService;
import ru.practicum.requests.dto.ParticipationRequestDto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getAllEventByUserIdForOwner(@PathVariable("userId") Long userId,
                                                           @PositiveOrZero @RequestParam(required = false, defaultValue = "0") Integer from,
                                                           @Positive @RequestParam(required = false, defaultValue = "10") Integer size) {
        log.info("Get запрос к /users/{userId}/events с параметрами userId: {}", userId);
        return eventService.getAllEventByUserIdForOwner(userId, from, size);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventFullDto createEventForOwner(@Validated @RequestBody UpdateEventUserRequest updateEventUserRequest,
                                            @PathVariable("userId") Long userId) {
        log.info("Post запрос к /users/{userId}/events с параметрами userId: {}", userId);
        return eventService.createEventForOwner(updateEventUserRequest, userId);
    }

    @GetMapping(path = "/{eventId}")
    public EventFullDto getEventByIdForOwner(@PathVariable("userId") Long userId,
                                             @PathVariable("eventId") Long eventId) {
        log.info("Get запрос к /users/{userId}/events/{eventId} с параметрами userId: {}, eventId: {}", userId, eventId);
        return eventService.getEventByIdForOwner(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}")
    public EventFullDto updateEventByIdForOwner(@Validated @RequestBody UpdateEventUserRequest updateEventUserRequest,
                                                @PathVariable("userId") Long userId,
                                                @PathVariable("eventId") Long eventId) {
        log.info("Patch запрос к /users/{userId}/events/{eventId} с параметрами userId: {}, eventId: {}", userId, eventId);
        return eventService.updateEventByIdForOwner(updateEventUserRequest, userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByEventIdForOwner(@PathVariable("userId") Long userId,
                                                                      @PathVariable("eventId") Long eventId) {
        log.info("Get запрос к /users/{userId}/events/{eventId}/requests с параметрами userId: {}, eventId: {}",
                userId, eventId);
        return eventService.getRequestsByEventIdForOwner(userId, eventId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventStatusForOwner(@NotNull @PathVariable Long userId,
                                                                    @NotNull @PathVariable Long eventId,
                                                                    @RequestBody(required = false) EventRequestStatusUpdateRequest request) {
        log.info("Patch запрос к /users/{userId}/events/{eventId}/requests с параметрами userId: {}, eventId: {}",
                userId, eventId);
        return eventService.updateEventStatusForOwner(userId, eventId, request);
    }
}