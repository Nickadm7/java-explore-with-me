package ru.practicum.ewmservice.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.ewmservice.categories.dto.CategoryDto;
import ru.practicum.ewmservice.user.dto.UserShortDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventShortDto {
    private Long id;

    @NotNull
    private String annotation;

    @NotNull
    private CategoryDto category;

    private Integer confirmedRequests;

    @NotNull
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;

    @NotNull
    private UserShortDto initiator;

    @NotNull
    private Boolean paid;

    @NotNull
    private String title;

    @NotNull
    private Long views;
}