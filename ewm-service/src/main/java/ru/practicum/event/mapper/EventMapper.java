package ru.practicum.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.State;
import ru.practicum.requests.model.ParticipationRequest;
import ru.practicum.user.mapper.UserMapper;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventMapper {

    public static Event newEventDtoToEvent(UpdateEventUserRequest updateEventUserRequest,
                                           User initiator,
                                           Category category) {
        return Event.builder()
                .annotation(updateEventUserRequest.getAnnotation())
                .category(category)
                .createdOn(LocalDateTime.now())
                .description(updateEventUserRequest.getDescription())
                .eventDate(updateEventUserRequest.getEventDate())
                .initiator(initiator)
                .location(updateEventUserRequest.getLocation())
                .paid(updateEventUserRequest.getPaid())
                .participantLimit(updateEventUserRequest.getParticipantLimit())
                .requestModeration(updateEventUserRequest.getRequestModeration())
                .title(updateEventUserRequest.getTitle())
                .publishedOn(LocalDateTime.now())
                .state(State.PENDING)
                .build();
    }

    public static EventFullDto eventToEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.userToUserDto(event.getInitiator()))
                .location(LocationMapper.locationToLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static List<EventShortDto> eventToEventShortDtoList(List<Event> events) {
        return events.stream()
                .map(EventMapper::eventToEventShortDto)
                .collect(Collectors.toList());
    }

    public static EventShortDto eventToEventShortDto(Event event) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(event.getViews())
                .build();
    }

    public static EventFullDto constructorToEventFullDto(Event event,
                                                         List<ParticipationRequest> confirmedRequests,
                                                         Map<Long, Long> views) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests.size())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.userToUserDto(event.getInitiator()))
                .location(LocationMapper.locationToLocationDto(event.getLocation()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .title(event.getTitle())
                .views(views.getOrDefault(event.getId(), 0L))
                .build();
    }

    public static List<EventShortDto> toEventShortDtoMap(List<Event> events,
                                                         Map<Event, List<ParticipationRequest>> confirmedRequests,
                                                         Map<Long, Long> views) {
        return events.stream()
                .map(event -> toEventShortDtoList(
                        event,
                        confirmedRequests.getOrDefault(event, List.of()),
                        views
                ))
                .collect(Collectors.toList());
    }

    public static EventShortDto toEventShortDtoList(Event event,
                                                    List<ParticipationRequest> confirmedRequests,
                                                    Map<Long, Long> views) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(confirmedRequests.size())
                .eventDate(event.getEventDate())
                .initiator(UserMapper.userToUserShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .views(views.getOrDefault(event.getId(), 0L))
                .build();
    }
}