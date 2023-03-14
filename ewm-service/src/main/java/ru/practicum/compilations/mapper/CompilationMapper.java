package ru.practicum.compilations.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.InputCompilationDto;
import ru.practicum.event.mapper.EventMapper;
import ru.practicum.event.model.Event;
import ru.practicum.compilations.model.Compilation;

import java.util.List;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CompilationMapper {
    public static Compilation newCompilationDtoToCompilation(InputCompilationDto inputCompilationDto, List<Event> events) {
        Compilation compilation = new Compilation();
        compilation.setPinned(inputCompilationDto.getPinned());
        compilation.setTitle(inputCompilationDto.getTitle());
        compilation.setEvents(events);
        return compilation;
    }

    public static CompilationDto compilationToCompilationDto(Compilation compilation) {
        return CompilationDto.builder()
                .id(compilation.getId())
                .pinned(compilation.getPinned())
                .title(compilation.getTitle())
                .events(EventMapper.eventToEventShortDtoList(compilation.getEvents()))
                .build();
    }
}