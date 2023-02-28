package ru.practicum.ewmservice.categories.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class CategoryDto {
    private Long id;
    @NotNull
    private String name;
}