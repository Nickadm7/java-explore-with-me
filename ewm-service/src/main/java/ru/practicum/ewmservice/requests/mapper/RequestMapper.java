package ru.practicum.ewmservice.requests.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.requests.dto.ParticipationRequestDto;
import ru.practicum.ewmservice.requests.model.ParticipationRequest;

import java.util.List;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestMapper {
    public static List<ParticipationRequestDto> RequestToRequestDtoList(List<ParticipationRequest> requests) {
        return requests.stream()
                .map(RequestMapper::RequestToRequestDto)
                .collect(Collectors.toList());
    }

    public static ParticipationRequestDto RequestToRequestDto(ParticipationRequest request) {
        return ParticipationRequestDto.builder()
                .id(request.getId())
                .created(request.getCreated())
                .event(request.getEvent().getId())
                .requester(request.getRequester().getId())
                .status(request.getStatus())
                .build();
    }
}
