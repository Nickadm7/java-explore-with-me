package ru.practicum.ewmservice.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.categories.mapper.CategoryMapper;
import ru.practicum.ewmservice.categories.model.Category;
import ru.practicum.ewmservice.compilations.service.dto.EventFullDto;
import ru.practicum.ewmservice.compilations.service.dto.EventShortDto;
import ru.practicum.ewmservice.compilations.service.dto.NewEventDto;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;
import ru.practicum.ewmservice.user.mapper.UserMapper;
import ru.practicum.ewmservice.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static Event newEventDtoToEvent(NewEventDto newEventDto, User initiator, Category category) {
        Event event = new Event();

        event.setAnnotation(newEventDto.getAnnotation());
        event.setCategory(category);
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setInitiator(initiator);
        event.setLocation(newEventDto.getLocation());
        event.setPaid(newEventDto.getPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.getRequestModeration());
        event.setTitle(newEventDto.getTitle());
        event.setPublishedOn(LocalDateTime.now());
        event.setState(State.PENDING);

       return event;
    }

    public static EventFullDto EventToEventFullDto(Event event) {
        return EventFullDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(CategoryMapper.categoryToCategoryDto(event.getCategory()))
                .confirmedRequests(event.getConfirmedRequests())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .eventDate(event.getEventDate())
                .initiator(event.getInitiator())
                .location(event.getLocation())
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
}