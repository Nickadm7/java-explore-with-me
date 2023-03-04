package ru.practicum.ewmservice.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewmservice.categories.model.Category;
import ru.practicum.ewmservice.categories.repository.CategoryRepository;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventShortDto;
import ru.practicum.ewmservice.event.dto.NewEventDto;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.mapper.LocationMapper;
import ru.practicum.ewmservice.event.model.*;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.ValidationException;
import ru.practicum.ewmservice.exception.WrongTimeException;
import ru.practicum.ewmservice.exception.WrongUpdatedEventException;
import ru.practicum.ewmservice.requests.dto.ParticipationRequestDto;
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
    @Transactional
    public EventFullDto updateEventByIdForAdmin(Long eventId, UpdateEventAdminRequest newEvent) {
        if (newEvent.getEventDate() != null) {
            if (newEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(1))) {
                throw new WrongTimeException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
            }
        }
        Event eventForUpdate = getEventByIdFromRepository(eventId);
        if (newEvent.getAnnotation() != null) {
            eventForUpdate.setAnnotation(newEvent.getAnnotation());
        }
        if (newEvent.getCategory() != null) {
            Category updatedCategory = getCategoryByIdFromRepository(newEvent.getCategory());
            eventForUpdate.setCategory(updatedCategory);
        }
        if (newEvent.getDescription() != null) {
            eventForUpdate.setDescription(newEvent.getDescription());
        }
        if (newEvent.getEventDate() != null) {
            eventForUpdate.setEventDate(newEvent.getEventDate());
        }
        if (newEvent.getLocation() != null) {
            eventForUpdate.setLocation(LocationMapper.locationDtoToLocation(newEvent.getLocation()));
        }
        if (newEvent.getPaid() != null) {
            eventForUpdate.setPaid(newEvent.getPaid());
        }
        if (newEvent.getParticipantLimit() != null) {
            eventForUpdate.setParticipantLimit(newEvent.getParticipantLimit());
        }
        if (newEvent.getRequestModeration() != null) {
            eventForUpdate.setRequestModeration(newEvent.getRequestModeration());
        }
        if (newEvent.getTitle() != null) {
            eventForUpdate.setTitle(newEvent.getTitle());
        }
        StateAction stateAction = newEvent.getStateAction();
        if (stateAction != null) {
            switch (stateAction) {
                case PUBLISH_EVENT:
                    if (eventForUpdate.getState().equals(State.PENDING)) {
                        eventForUpdate.setState(State.PUBLISHED);
                        eventForUpdate.setPublishedOn(LocalDateTime.now());
                    } else
                        throw new WrongUpdatedEventException("Нельзя опубликовать статус не PENDING");
                    break;
                case CANCEL_REVIEW:
                    if (!eventForUpdate.getState().equals(State.PUBLISHED)) {
                        eventForUpdate.setState(State.CANCELED);
                    } else
                        throw new WrongUpdatedEventException("Нельзя отменить неопубликованное");
                    break;
                case REJECT_EVENT:
                    if (!eventForUpdate.getState().equals(State.PUBLISHED)) {
                        eventForUpdate.setState(State.CANCELED);
                    } else
                        throw new WrongUpdatedEventException("Нельзя отклонить неопубликованное");
                    break;
                case SEND_TO_REVIEW:
                    eventForUpdate.setState(State.PENDING);
                    break;
            }
        }
        Event out = eventRepository.save(eventForUpdate);
        log.info("Успешно обновлен админом Event с id: {}", eventForUpdate.getId());
        return EventMapper.EventToEventFullDto(out);
    }

    @Override
    public List<EventShortDto> getAllEventByUserIdForOwner(Long userId) {
        return null;
    }

    @Override
    @Transactional
    public EventFullDto createEventForOwner(NewEventDto newEventDto, Long userId) {
        if (newEventDto.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("EventDate раньше текущего времени + 2 часа");
        }
        User initiator = getUserByIdFromRepository(userId);
        Category category = getCategoryByIdFromRepository(newEventDto.getCategory());
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

    private User getUserByIdFromRepository(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                });
    }

    private Category getCategoryByIdFromRepository(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                });
    }

    private Event getEventByIdFromRepository(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                });
    }
}