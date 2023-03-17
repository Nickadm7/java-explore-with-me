package ru.practicum.requests.service;

import ru.practicum.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getAllRequestForCurrentUser(Long userId);

    ParticipationRequestDto createRequestForCurrentUser(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestForOwner(Long userId, Long requestId);
}
