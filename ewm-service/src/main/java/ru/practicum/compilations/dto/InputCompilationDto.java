package ru.practicum.compilations.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class InputCompilationDto {
    private List<Long> events; //Список идентификаторов событий входящих в подборку

    private Boolean pinned; //Закреплена ли подборка на главной странице сайта

    @NotNull
    @NotBlank
    private String title; //Заголовок подборки
}
