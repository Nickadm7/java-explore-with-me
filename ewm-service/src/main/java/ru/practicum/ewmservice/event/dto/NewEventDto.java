package ru.practicum.ewmservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.ewmservice.categories.dto.CategoryDto;
import ru.practicum.ewmservice.event.model.Location;
import ru.practicum.ewmservice.user.model.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewEventDto {
    @NotNull
    private String annotation; //Краткое описание

    @NotNull
    private CategoryDto category;

    @NotNull
    private String description; //Полное описание события

    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate; //Дата и время на которые намечено событие (в формате "yyyy-MM-dd HH:mm:ss")

    @NotNull
    private User initiator; //Инициатор события

    @NotNull
    private Location location; //Координаты места проведения

    private Boolean paid; //Нужно ли оплачивать участие

    private Integer participantLimit; //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения

    private LocalDateTime publishedOn; //Дата и время публикации события (в формате "yyyy-MM-dd HH:mm:ss")

    private Boolean requestModeration; //Нужна ли пре-модерация заявок на участие

    @NotNull
    private String title; //Заголовок
}