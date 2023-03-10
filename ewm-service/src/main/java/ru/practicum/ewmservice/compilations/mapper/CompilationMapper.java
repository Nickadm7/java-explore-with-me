package ru.practicum.ewmservice.compilations.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewmservice.compilations.dto.CompilationDto;
import ru.practicum.ewmservice.compilations.dto.InputCompilationDto;
import ru.practicum.ewmservice.compilations.model.Compilation;
import ru.practicum.ewmservice.event.mapper.EventMapper;
import ru.practicum.ewmservice.event.model.Event;

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