package ru.practicum.ewmservice.compilations.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class NewCompilationDto {
    private List<Long> events; //Список идентификаторов событий входящих в подборку

    private Boolean pinned; //Закреплена ли подборка на главной странице сайта

    private String title; //Заголовок подборки
}
