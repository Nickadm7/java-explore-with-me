package ru.practicum.ewmservice.user.dto;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
@Builder(toBuilder = true)
@RequiredArgsConstructor
public class InputUserDto {
    @NotBlank
    private final String name;
    @Email
    private final String email;
}
