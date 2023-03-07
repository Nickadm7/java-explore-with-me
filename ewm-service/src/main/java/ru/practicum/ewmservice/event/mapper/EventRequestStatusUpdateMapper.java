package ru.practicum.ewmservice.event.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.event.model.EventRequestStatusUpdateResult;
import ru.practicum.ewmservice.event.model.Status;
import ru.practicum.ewmservice.requests.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.requests.mapper.RequestMapper;
import ru.practicum.ewmservice.requests.model.ParticipationRequest;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventRequestStatusUpdateMapper {
    public static EventRequestStatusUpdateResult toEventRequestStatusUpdateResult(List<ParticipationRequest> requests) {
        List<ParticipationRequestDto> requestsDto = RequestMapper.RequestToRequestDtoList(requests);
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