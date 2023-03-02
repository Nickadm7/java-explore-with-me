package ru.practicum.ewmservice.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.ewmservice.compilations.dto.CompilationDto;
import ru.practicum.ewmservice.compilations.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilations.mapper.CompilationMapper;
import ru.practicum.ewmservice.compilations.model.Compilation;
import ru.practicum.ewmservice.compilations.repository.CompilationRepository;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.repository.EventRepository;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService{
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;
    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        return null;
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        return null;
    }

    @Override
    public CompilationDto createCompilationForAdmin(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilationToSave = CompilationMapper.newCompilationDtoToCompilation(newCompilationDto, events);

        Compilation out = compilationRepository.save(compilationToSave);
        log.info("Сохранен Compilation id: {}, pinned: {}, title: {}", out.getId(),
                out.getPinned(), out.getTitle());
        System.out.println(out.getEvents().get(0).toString());
        return CompilationMapper.compilationToCompilationDto(out);
    }

    @Override
    public CompilationDto deleteCompilationForAdmin(Long compId) {
        return null;
    }

    @Override
    public CompilationDto updateCompilationForAdmin(NewCompilationDto newCompilationDto, Long compId) {
        return null;
    }
}