package ru.practicum.ewmservice.event.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.compilations.service.dto.EventFullDto;
import ru.practicum.ewmservice.event.model.UpdateEventAdminRequest;
import ru.practicum.ewmservice.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getAllEventsUseFilterForAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) LocalDateTime rangeStart,
            @RequestParam(required = false) LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero int from,
            @RequestParam(required = false, defaultValue = "10") @PositiveOrZero int size) {
        log.info("Get запрос к /admin/events с параметрами users: {}, states: {}, categories: {}," +
                        " rangeStart: {}, rangeEnd: {}, from: {}, size: {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getAllEventsUseFilterForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventByIdForAdmin(
            @PathVariable @Positive Long eventId,
            @RequestBody @Valid UpdateEventAdminRequest updateEventAdminRequest) {
        log.info("Get запрос к /admin/events/{eventId} с параметрами eventId: {}", eventId);
        return eventService.updateEventByIdForAdmin(eventId, updateEventAdminRequest);
    }
}