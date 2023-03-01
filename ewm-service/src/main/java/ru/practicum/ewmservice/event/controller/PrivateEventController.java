package ru.practicum.ewmservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventShortDto;
import ru.practicum.ewmservice.event.dto.NewEventDto;
import ru.practicum.ewmservice.event.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.event.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewmservice.event.model.EventRequestStatusUpdateResult;
import ru.practicum.ewmservice.event.service.EventService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getAllEventByUserIdForOwner(@PathVariable("userId") Long userId) {
        log.info("Get запрос к /users/{userId}/events с параметрами userId: {}", userId);
        return eventService.getAllEventByUserIdForOwner(userId);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public EventFullDto createEventForOwner(@Validated @RequestBody NewEventDto newEventDto,
                                            @PathVariable("userId") Long userId) {
        log.info("Post запрос к /users/{userId}/events с параметрами userId: {}", userId);
        return eventService.createEventForOwner(newEventDto, userId);
    }

    @GetMapping(path = "/{eventId}")
    public EventFullDto getEventByIdForOwner(@PathVariable("userId") Long userId,
                                             @PathVariable("eventId") Long eventId) {
        log.info("Get запрос к /users/{userId}/events/{eventId} с параметрами userId: {}, eventId: {}", userId, eventId);
        return eventService.getEventByIdForOwner(userId, eventId);
    }

    @PatchMapping(path = "/{eventId}")
    public EventFullDto updateEventByIdForOwner(@Validated @RequestBody NewEventDto newEventDto,
                                                @PathVariable("userId") Long userId,
                                                @PathVariable("eventId") Long eventId) {
        log.info("Patch запрос к /users/{userId}/events/{eventId} с параметрами userId: {}, eventId: {}", userId, eventId);
        return eventService.updateEventByIdForOwner(newEventDto, userId, eventId);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getRequestsByEventIdForOwner(@PathVariable("userId") Long userId,
                                                                      @PathVariable("eventId") Long eventId) {
        log.info("Get запрос к /users/{userId}/events/{eventId}/requests с параметрами userId: {}, eventId: {}",
                userId, eventId);
        return eventService.getRequestsByEventIdForOwner(userId, eventId);
    }

    @PatchMapping("/events/{eventId}/requests")
    public EventRequestStatusUpdateResult updateEventStatusForOwner(@NotNull @PathVariable Long userId,
                                                                    @NotNull @PathVariable Long eventId,
                                                                    @RequestBody EventRequestStatusUpdateRequest request) {
        log.info("Patch запрос к /users/{userId}/events/{eventId}/requests с параметрами userId: {}, eventId: {}",
                userId, eventId);
        return eventService.updateEventStatusForOwner(userId, eventId, request);
    }
}