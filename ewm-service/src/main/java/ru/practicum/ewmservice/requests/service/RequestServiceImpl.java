package ru.practicum.ewmservice.requests.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.model.State;
import ru.practicum.ewmservice.event.model.Status;
import ru.practicum.ewmservice.event.repository.EventRepository;
import ru.practicum.ewmservice.exception.ValidationException;
import ru.practicum.ewmservice.requests.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.requests.mapper.RequestMapper;
import ru.practicum.ewmservice.requests.model.ParticipationRequest;
import ru.practicum.ewmservice.requests.repository.RequestRepository;
import ru.practicum.ewmservice.user.model.User;
import ru.practicum.ewmservice.user.repository.UserRepository;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public List<ParticipationRequestDto> getAllRequestForCurrentUser(Long userId) {
        User user = getUserByIdFromRepository(userId);
        List<ParticipationRequest> out = requestRepository.findAll_ByRequester(user);
        log.info("Получены все запросы от User id: {}", userId);
        return RequestMapper.RequestToRequestDtoList(out);
    }

    @Override
    @Transactional
    public ParticipationRequestDto createRequestForCurrentUser(Long userId, Long eventId) {
        User requester = getUserByIdFromRepository(userId);
        Event event = getEventByIdFromRepository(eventId);
        if (event.getInitiator().getId().equals(requester.getId())) {
            throw new ValidationException("Нельзя создавать запрос к своему Event");
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ValidationException("Нельзя создавать запрос к неопубликованному Event");
        }
        Integer countConfirmed = requestRepository.findAll_ByEvent_IdAndStatus(event.getId(),
                Status.CONFIRMED).size();
        if (event.getParticipantLimit() <= countConfirmed) {
            throw new ValidationException("Все места на Event заняты");
        }
        ParticipationRequest existRequest = requestRepository.findByRequester_idAndEvent_id(userId, eventId);
        if (existRequest != null) {
            throw new ValidationException("Ошибка добавления повторного запроса");
        }
        ParticipationRequest requestToSave = new ParticipationRequest();
        requestToSave.setCreated(LocalDateTime.now());
        requestToSave.setEvent(event);
        requestToSave.setRequester(requester);
        if (event.getRequestModeration() && event.getParticipantLimit() != 0) {
            requestToSave.setStatus(Status.PENDING);
        } else {
            requestToSave.setStatus(Status.CONFIRMED);
        }
        ParticipationRequest out = requestRepository.save(requestToSave);
        log.info("Успешно создан запрос c id = {} ", out.getId());
        return RequestMapper.RequestToRequestDto(out);
    }

    @Override
    @Transactional
    public ParticipationRequestDto cancelRequestForOwner(Long userId, Long requestId) {
        User user = getUserByIdFromRepository(userId);
        ParticipationRequest request = getEventRequestByRequestId(requestId);
        if (!user.getId().equals(request.getRequester().getId())) {
            throw new ValidationException("Нельзя отменять чужой запрос");
        }
        request.setStatus(Status.CANCELED);
        log.info("Успешно отменен Request c id: {}", requestId);
        return RequestMapper.RequestToRequestDto(request);
    }

    private User getUserByIdFromRepository(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> {
                    throw new ValidationException("Не найден пользователь по id");
                });
    }

    private ParticipationRequest getEventRequestByRequestId(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> {
                    throw new ValidationException("Не найден EventRequest по id");
                });
    }

    private Event getEventByIdFromRepository(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    throw new ValidationException("Не найден Event по id");
                });
    }
}