package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.event.model.StateAction;
import ru.practicum.event.model.Location;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UpdateEventUserRequest {
    private String annotation; //Краткое описание

    private Long category;

    private String description; //Полное описание события

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; //Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    private Location location; //Координаты места проведения

    private Boolean paid; //Нужно ли оплачивать участие

    private Integer participantLimit; //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие

    private String title; //Заголовок

    private StateAction stateAction;
}