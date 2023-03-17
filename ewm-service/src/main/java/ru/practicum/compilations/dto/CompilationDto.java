package ru.practicum.compilations.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.event.dto.EventShortDto;

import java.util.List;

@Getter
@Setter
@Builder(toBuilder = true)
public class CompilationDto {
    private Long id;

    private Boolean pinned;

    private String title;

    private List<EventShortDto> events;

    @Override
    public String toString() {
        return "CompilationDto{" +
                "id=" + id +
                ", pinned=" + pinned +
                ", title='" + title + '\'' +
                ", events=" + events +
                '}';
    }
}