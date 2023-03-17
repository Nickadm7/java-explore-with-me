package ru.practicum.user.dto;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UserShortDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
}