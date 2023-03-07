package ru.practicum.ewmservice.event.model;

import lombok.*;
import ru.practicum.ewmservice.requests.dto.ParticipationRequestDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class EventRequestStatusUpdateResult { //Результат подтверждения/отклонения заявок на участие в событии
    private List<ParticipationRequestDto> confirmedRequests;

    private List<ParticipationRequestDto> rejectedRequests;
}