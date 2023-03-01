package ru.practicum.ewmservice.event.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestStatusUpdateRequest { //Изменение статуса запроса на участие в событии текущего пользователя
    private List<Long> requestIds;

    private Status status;
}
