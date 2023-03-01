package ru.practicum.ewmservice.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDto {
    @NotNull
    private Long id;
    @NotNull
    private String name;
}