package ru.practicum.ewmservice.event.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewmservice.categories.model.Category;
import ru.practicum.ewmservice.categories.repository.CategoryRepository;
import ru.practicum.ewmservice.event.dto.EventFullDto;
import ru.practicum.ewmservice.event.dto.EventShortDto;
import ru.practicum.ewmservice.event.dto.UpdateEventUserRequest;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.mapper.EventRequestStatusUpdateMapper;
import ru.practicum.ewmservice.event.mapper.LocationMapper;
import ru.practicum.ewmservice.event.model.*;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.ConflictException;
import ru.practicum.ewmservice.exception.ValidationException;
import ru.practicum.ewmservice.exception.WrongTimeException;
import ru.practicum.ewmservice.exception.WrongUpdatedEventException;
import ru.practicum.ewmservice.requests.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.requests.mapper.RequestMapper;
import ru.practicum.ewmservice.requests.model.ParticipationRequest;
import ru.practicum.ewmservice.requests.repository.RequestRepository;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Override
    public List<EventShortDto> searchEventUseFilter(String text,
                                                    List<Long> categories,
                                                    Boolean paid,
                                                    String rangeStart,
                                                    String rangeEnd,
                                                    Boolean onlyAvailable,
                                                    String sort,
                                                    int from,
                                                    int size,
                                                    HttpServletRequest request) {
        LocalDateTime rangeStartTime = getRangeStartTime(rangeStart);
        LocalDateTime rangeEndTime = getRangeEndTime(rangeEnd);
        if (rangeStartTime.isAfter(rangeEndTime)) {
            throw new ValidationException("Окончание раньше начала");
        }

        BooleanBuilder predicate = getPredicateForPublicSearch(text, categories, paid, rangeStartTime, rangeEndTime);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> eventsPredicateAndPageable = eventRepository.findAll(predicate, pageable).toList();
        List<Event> sortEventsByAvailable = sortEventsAvailableOrNot(eventsPredicateAndPageable, onlyAvailable);

        List<EventShortDto> eventShortDtos = sortEventsByAvailable.stream()
                .map(EventMapper::eventToEventShortDto)
                .collect(toList());

        List<EventShortDto> out = new ArrayList<>();
        if (sort.equals("EVENT_DATE")) {
            out = eventShortDtos;
        }
        if (sort.equals("VIEWS")) {
            out = eventShortDtos;
            out.sort(Comparator.comparing(EventShortDto::getViews).reversed());
        }
        log.info("Успешно получен список Event (для пользователей) с фильтрацией: {}", out);
        return out;
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        List<ParticipationRequest> confirmedRequests;
        Event event = getEventByIdFromRepository(eventId);
        confirmedRequests = requestRepository.findAll_ByEvent_IdAndStatus(eventId, Status.CONFIRMED);
        Map<Long, Long> views = new HashMap<>();
        //TODO
        log.info("Успешно получен Event по id: {}", eventId);
        return EventMapper.constructorToEventFullDto(event, confirmedRequests, views);
    }

    @Override
    public List<EventFullDto> getAllEventsUseFilterForAdmin(List<Long> users,
                                                            List<String> states,
                                                            List<Long> categories,
                                                            String rangeStart,
                                                            String rangeEnd,
                                                            int from,
                                                            int size) {
        LocalDateTime rangeStartTime = getRangeStartTime(rangeStart);
        LocalDateTime rangeEndTime = getRangeEndTime(rangeEnd);
        if (rangeStartTime.isAfter(rangeEndTime)) {
            throw new ValidationException("Окончание раньше начала");
        }
        BooleanBuilder predicate = getPredicateForAdminSearch(users, states, categories, rangeStartTime, rangeEndTime);
        Pageable pageable = PageRequest.of(from / size, size);
        List<Event> eventsPredicateAndPageable = eventRepository.findAll(predicate, pageable).toList();
        List<EventFullDto> eventsFullDto = eventsPredicateAndPageable.stream()
                .map(EventMapper::eventToEventFullDto)
                .collect(toList());
        log.info("Успешно получен список Event (для падмина) с фильтрацией: {}", eventsFullDto);
        return eventsFullDto;
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
        return EventMapper.eventToEventFullDto(out);
    }

    @Override
    public List<EventShortDto> getAllEventByUserIdForOwner(Long userId, Integer from, Integer size) {
        Map<Long, Long> views = new HashMap<>();
        Map<Event, List<ParticipationRequest>> confirmedRequests;
        User initiator = getUserByIdFromRepository(userId);
        List<Event> events = eventRepository.findAllByInitiator(initiator);
        confirmedRequests = findConfirmedRequestsMap(events);
        log.info("Сформирован список ивентов пользователя c id = {} ", initiator);
        return EventMapper.toEventShortDtoMap(events, confirmedRequests, views);
    }

    @Override
    @Transactional
    public EventFullDto createEventForOwner(UpdateEventUserRequest updateEventUserRequest, Long userId) {
        if (updateEventUserRequest.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
            throw new ValidationException("EventDate раньше текущего времени + 2 часа");
        }
        User initiator = getUserByIdFromRepository(userId);
        Category category = getCategoryByIdFromRepository(updateEventUserRequest.getCategory());
        Event eventToSave = EventMapper.newEventDtoToEvent(updateEventUserRequest, initiator, category);
        Event outEvent = eventRepository.save(eventToSave);
        log.info("Сохранен Event id: {}, initiatorId: {}, categoryId: {}", outEvent.getId(),
                initiator.getId(), category.getId());
        return EventMapper.eventToEventFullDto(outEvent);
    }

    @Override
    public EventFullDto getEventByIdForOwner(Long userId, Long eventId) {
        getUserByIdFromRepository(userId); //проверка что пользователь существует
        Event event = getEventByIdFromRepository(eventId);
        checkUserCanUpdateAndViewEvent(userId, event);
        List<ParticipationRequest> confirmedRequests = requestRepository.findAll_ByEvent_IdAndStatus(eventId, Status.CONFIRMED);
        Map<Long, Long> views = new HashMap<>(); //TODO
        log.info("Получено событие с id: {} в формате EventFullDto", eventId);
        return EventMapper.constructorToEventFullDto(event, confirmedRequests, views);
    }

    @Override
    public EventFullDto updateEventByIdForOwner(UpdateEventUserRequest updateEvent, Long userId, Long eventId) {
        getUserByIdFromRepository(userId); //проверка что пользователь существует
        Event event = getEventByIdFromRepository(eventId);
        checkUserCanUpdateAndViewEvent(userId, event);
        if (event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Нельзя обновить Event, у которого статус опубликован");
        }
        if (updateEvent.getEventDate() != null) {
            if (updateEvent.getEventDate().isBefore(LocalDateTime.now().plusHours(2))) {
                throw new WrongTimeException("Нельзя обновить Event за 2 часа до начала");
            }
            event.setEventDate(updateEvent.getEventDate());
        }
        if (updateEvent.getTitle() != null) {
            event.setTitle(updateEvent.getTitle());
        }
        if (updateEvent.getAnnotation() != null) {
            event.setAnnotation(updateEvent.getAnnotation());
        }
        if (updateEvent.getDescription() != null) {
            event.setDescription(updateEvent.getDescription());
        }
        if (updateEvent.getCategory() != null) {
            Category categoryToUpdate = getCategoryByIdFromRepository(updateEvent.getCategory());
            event.setCategory(categoryToUpdate);
        }
        if (updateEvent.getPaid() != null) {
            event.setPaid(updateEvent.getPaid());
        }
        if (updateEvent.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEvent.getParticipantLimit());
        }
        if (updateEvent.getStateAction() != null) {
            switch (updateEvent.getStateAction()) {
                case SEND_TO_REVIEW:
                    event.setState(State.PENDING);
                    break;
                case CANCEL_REVIEW:
                    event.setState(State.CANCELED);
                    break;
            }
        }
        Event out = eventRepository.save(event);
        log.info("Успешно обновлен Event с id: {}, пользователем с id: {}", eventId, userId);
        return EventMapper.eventToEventFullDto(out);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByEventIdForOwner(Long userId, Long eventId) {
        Event event = getEventByIdFromRepository(eventId);
        checkUserCanUpdateAndViewEvent(userId, event);
        List<ParticipationRequest> out = requestRepository.findAll_ByEvent(event);
        log.info("Получен список запросов на Event с id: {}", eventId);
        return RequestMapper.RequestToRequestDtoList(out);
    }

    @Override
    public EventRequestStatusUpdateResult updateEventStatusForOwner(Long userId,
                                                                    Long eventId,
                                                                    EventRequestStatusUpdateRequest request) {
        if (request == null) throw new ConflictException("Ошибка в данных Request");
        getUserByIdFromRepository(userId); //проверка что пользователь существует
        Event event = getEventByIdFromRepository(eventId);
        checkUserCanUpdateAndViewEvent(userId, event);
        List<ParticipationRequest> requests = requestRepository.findAllByIdIn(request.getRequestIds());
        if (requests.isEmpty()) {
            return new EventRequestStatusUpdateResult();
        }
        checkEventIsNotFull(event);
        List<ParticipationRequest> outRequests = requests.stream()
                .filter(r -> r.getStatus().equals(Status.PENDING))
                .collect(toList());
        int countConfirmedRequest = requestRepository.findAllByEvent_Id(event.getId()).size();
        int delta = event.getParticipantLimit() - countConfirmedRequest;
        if (delta < 1) {
            throw new ConflictException("Нет доступных мест в Event");
        }
        switch (request.getStatus()) {
            case REJECTED:
                requestsSetStatus(outRequests, Status.REJECTED);
                break;
            case CONFIRMED:
                if (event.getParticipantLimit() == 0) {
                    requestsSetStatus(outRequests, Status.CONFIRMED);
                } else {
                    countConfirmedRequest = requestRepository.findAllByEvent_Id(event.getId()).size();
                    delta = event.getParticipantLimit() - countConfirmedRequest;
                    if (outRequests.size() <= delta) {
                        requestsSetStatus(outRequests, Status.CONFIRMED);
                    } else if (delta > 0) {
                        requestsSetStatus(outRequests.subList(0, delta - 1), Status.CONFIRMED);
                        requestsSetStatus(outRequests.subList(delta, requests.size()), Status.REJECTED);
                    } else {
                        requestsSetStatus(outRequests, Status.REJECTED);
                    }
                }
        }
        log.info("Успешно обновлены статусы Event с id: {}", request.getRequestIds());
        return EventRequestStatusUpdateMapper.toEventRequestStatusUpdateResult(outRequests);
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

    private void checkUserCanUpdateAndViewEvent(Long userId, Event event) {
        if (!userId.equals(event.getInitiator().getId())) {
            throw new ValidationException("Нельзя обновить Event не его владельцу");
        }
    }

    private Map<Event, List<ParticipationRequest>> findConfirmedRequestsMap(List<Event> events) {
        List<Long> eventsIds = events.stream()
                .map(Event::getId)
                .collect(toList());
        List<ParticipationRequest> confirmedRequests = requestRepository.findAll_ByEvent_IdsList(eventsIds);
        return confirmedRequests.stream()
                .collect(Collectors.groupingBy(ParticipationRequest::getEvent, toList()));
    }

    private void checkEventIsNotFull(Event event) {
        Integer countConfirmed = requestRepository.findAll_ByEvent_IdAndStatus(event.getId(),
                Status.CONFIRMED).size();
        if (event.getParticipantLimit() <= countConfirmed) {
            throw new ValidationException("Все места на Event заняты");
        }
    }

    private void requestsSetStatus(List<ParticipationRequest> requests, Status status) {
        requests.forEach(r -> r.setStatus(status));
    }

    private BooleanBuilder getPredicateForPublicSearch(String text,
                                                       List<Long> categories,
                                                       Boolean paid,
                                                       LocalDateTime rangeStart,
                                                       LocalDateTime rangeEnd) {
        BooleanBuilder outPredicate = new BooleanBuilder();
        if (text != null) {
            outPredicate.and(QEvent.event.annotation.likeIgnoreCase(text)
                    .or(QEvent.event.description.likeIgnoreCase(text)));
        }
        if (!categories.isEmpty()) {
            outPredicate.and(QEvent.event.category.id.in(categories));
        }
        if (paid != null) {
            outPredicate.and(QEvent.event.paid.eq(paid));
        }
        outPredicate.and(QEvent.event.eventDate.after(rangeStart));
        outPredicate.and(QEvent.event.eventDate.before(rangeEnd));
        return outPredicate;
    }

    private List<Event> sortEventsAvailableOrNot(List<Event> events, Boolean onlyAvailable) {
        List<Event> outEvents;
        if (onlyAvailable) {
            outEvents = events.stream()
                    .filter(e -> e.getConfirmedRequests() < e.getParticipantLimit())
                    .collect(toList());
            return outEvents;
        } else {
            return events;
        }
    }

    private BooleanBuilder getPredicateForAdminSearch(List<Long> users,
                                                      List<String> states,
                                                      List<Long> categories,
                                                      LocalDateTime rangeStart,
                                                      LocalDateTime rangeEnd) {
        BooleanBuilder outPredicate = new BooleanBuilder();
        List<State> stateList = new ArrayList<>();
        if (states != null) {
            stateList = states.stream().map(State::valueOf).collect(toList());
        }
        if (users != null && !users.isEmpty()) {
            outPredicate.and(QEvent.event.initiator.id.in(users));
        }
        if (states != null) {
            outPredicate.and(QEvent.event.state.in(stateList));
        }
        if (categories != null && !categories.isEmpty()) {
            outPredicate.and(QEvent.event.category.id.in(categories));
        }
        outPredicate.and(QEvent.event.eventDate.after(rangeStart));
        outPredicate.and(QEvent.event.eventDate.before(rangeEnd));
        return outPredicate;
    }

    private LocalDateTime getRangeStartTime(String rangeStart) {
        if (rangeStart != null) {
            return LocalDateTime.parse(rangeStart, formatter);
        } else {
            return LocalDateTime.now();
        }
    }

    private LocalDateTime getRangeEndTime(String rangeEnd) {
        if (rangeEnd != null) {
            return LocalDateTime.parse(rangeEnd, formatter);
        } else {
            return LocalDateTime.now().plusYears(111);
        }
    }
}