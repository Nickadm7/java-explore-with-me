package ru.practicum.ewmservice.event.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class LocationDto {
    @NotBlank
    private Double lat;
    @NotBlank
    private Double lon;
}
