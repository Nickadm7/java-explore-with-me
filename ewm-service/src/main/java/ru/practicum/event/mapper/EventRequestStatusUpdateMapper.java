package ru.practicum.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.event.model.EventRequestStatusUpdateResult;
import ru.practicum.event.model.Status;
import ru.practicum.requests.dto.ParticipationRequestDto;
import ru.practicum.requests.mapper.RequestMapper;
import ru.practicum.requests.model.ParticipationRequest;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class EventRequestStatusUpdateMapper {
    public static EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(List<ParticipationRequest> requests) {
        List<ParticipationRequestDto> requestsDto = RequestMapper.requestToRequestDtoList(requests);
        return EventRequestStatusUpdateResult.builder()
                .confirmedRequests(
                        requestsDto.stream()
                                .filter(r -> r.getStatus().equals(Status.CONFIRMED))
                                .collect(Collectors.toList())
                )
                .rejectedRequests(
                        requestsDto.stream()
                                .filter(r -> r.getStatus().equals(Status.REJECTED))
                                .collect(Collectors.toList())
                )
                .build();
    }
}