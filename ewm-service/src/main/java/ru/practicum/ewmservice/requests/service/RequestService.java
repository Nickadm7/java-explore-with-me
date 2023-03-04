package ru.practicum.ewmservice.requests.service;

import ru.practicum.ewmservice.requests.dto.ParticipationRequestDto;

import java.util.List;

public interface RequestService {
    List<ParticipationRequestDto> getAllRequestForCurrentUser(Long userId);

    ParticipationRequestDto createRequestForCurrentUser(Long userId, Long eventId);

    ParticipationRequestDto cancelRequestForOwner(Long userId, Long requestId);
}
