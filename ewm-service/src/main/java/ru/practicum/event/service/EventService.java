package ru.practicum.event.service;

import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.EventRequestStatusUpdateRequest;
import ru.practicum.event.model.EventRequestStatusUpdateResult;
import ru.practicum.event.model.UpdateEventAdminRequest;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.requests.dto.ParticipationRequestDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    List<EventShortDto> searchEventUseFilter(String text,
                                             List<Long> categories,
                                             Boolean paid,
                                             String rangeStart,
                                             String rangeEnd,
                                             Boolean onlyAvailable,
                                             String sort,
                                             int from,
                                             int size,
                                             HttpServletRequest request);

    EventFullDto getEventById(Long id, HttpServletRequest request);

    List<EventFullDto> getAllEventsUseFilterForAdmin(List<Long> users,
                                                     List<String> states,
                                                     List<Long> categories,
                                                     String rangeStart,
                                                     String rangeEnd,
                                                     int from,
                                                     int size);

    EventFullDto updateEventByIdForAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest);

    List<EventShortDto> getAllEventByUserIdForOwner(Long userId, Integer from, Integer size);

    EventFullDto createEventForOwner(UpdateEventUserRequest updateEventUserRequest, Long userId);

    EventFullDto getEventByIdForOwner(Long userId, Long eventId);

    EventFullDto updateEventByIdForOwner(UpdateEventUserRequest updateEventUserRequest, Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsByEventIdForOwner(Long userId, Long eventId);

    EventRequestStatusUpdateResult updateEventStatusForOwner(Long userId, Long eventId, EventRequestStatusUpdateRequest request);
}
