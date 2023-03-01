package ru.practicum.ewmservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventShortDto;
import ru.practicum.ewmservice.event.dto.NewEventDto;
import ru.practicum.ewmservice.event.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.event.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewmservice.event.model.EventRequestStatusUpdateResult;
import ru.practicum.ewmservice.event.model.UpdateEventAdminRequest;
import ru.practicum.ewmservice.event.repository.EventRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;

    @Override
    public List<EventShortDto> searchEventUseFilter(String text,
                                                    List<Long> categories,
                                                    Boolean paid,
                                                    LocalDateTime rangeStart,
                                                    LocalDateTime rangeEnd,
                                                    Boolean onlyAvailable,
                                                    String sort,
                                                    int from,
                                                    int size,
                                                    HttpServletRequest request) {
        return null;
    }

    @Override
    public EventFullDto getEventById(Long id, HttpServletRequest request) {
        return null;
    }

    @Override
    public List<EventFullDto> getAllEventsUseFilterForAdmin(List<Long> users,
                                                            List<String> states,
                                                            List<Long> categories,
                                                            LocalDateTime rangeStart,
                                                            LocalDateTime rangeEnd,
                                                            int from,
                                                            int size) {
        return null;
    }

    @Override
    public EventFullDto updateEventByIdForAdmin(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
        return null;
    }

    @Override
    public List<EventShortDto> getAllEventByUserIdForOwner(Long userId) {
        return null;
    }

    @Override
    public EventFullDto createEventForOwner(NewEventDto newEventDto, Long userId) {
        return null;
    }

    @Override
    public EventFullDto getEventByIdForOwner(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventFullDto updateEventByIdForOwner(NewEventDto newEventDto, Long userId, Long eventId) {
        return null;
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByEventIdForOwner(Long userId, Long eventId) {
        return null;
    }

    @Override
    public EventRequestStatusUpdateResult updateEventStatusForOwner(Long userId, Long eventId, EventRequestStatusUpdateRequest request) {
        return null;
    }
}