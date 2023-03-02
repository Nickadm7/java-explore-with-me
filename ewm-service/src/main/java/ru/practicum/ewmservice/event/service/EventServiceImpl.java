package ru.practicum.ewmservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewmservice.categories.model.Category;
import ru.practicum.ewmservice.categories.repository.CategoryRepository;
import ru.practicum.ewmservice.compilations.service.dto.EventFullDto;
import ru.practicum.ewmservice.compilations.service.dto.EventShortDto;
import ru.practicum.ewmservice.compilations.service.dto.NewEventDto;
import ru.practicum.ewmservice.compilations.service.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.EventRequestStatusUpdateRequest;
import ru.practicum.ewmservice.event.model.EventRequestStatusUpdateResult;
import ru.practicum.ewmservice.event.model.UpdateEventAdminRequest;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

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
    @Transactional
    public EventFullDto createEventForOwner(NewEventDto newEventDto, Long userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        User initiator = getUserById(userId);
        Category category = getCategoryById(newEventDto.getCategory());
        Event eventToSave = EventMapper.newEventDtoToEvent(newEventDto, initiator, category);
        Event outEvent = eventRepository.save(eventToSave);
        log.info("Сохранен Event id: {}, initiatorId: {}, categoryId: {}", outEvent.getId(),
                initiator.getId(), category.getId());
        return EventMapper.EventToEventFullDto(outEvent);
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

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                });
    }

    private Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                });
    }
}