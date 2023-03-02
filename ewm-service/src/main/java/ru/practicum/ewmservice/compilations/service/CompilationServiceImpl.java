package ru.practicum.ewmservice.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewmservice.compilations.dto.CompilationDto;
import ru.practicum.ewmservice.compilations.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilations.mapper.CompilationMapper;
import ru.practicum.ewmservice.compilations.model.Compilation;
import ru.practicum.ewmservice.compilations.repository.CompilationRepository;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.event.repository.EventRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {
    private final EventRepository eventRepository;
    private final CompilationRepository compilationRepository;

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilations;
        if (pinned == null) {
            compilations = compilationRepository.findAll();
        } else {
            compilations = compilationRepository.findCompilationByPinnedOrderById(pinned, pageable);
        }
        log.info("Получены все Compilation с параметрами pinned: {}, from: {}, size: {}", pinned, from, size);
        return compilations.stream()
                .map(CompilationMapper::compilationToCompilationDto)
                .collect(Collectors.toList());
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> {
                    log.info("Не найдена Compilation с id: {}", compId);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
        log.info("Найдена Compilation с id: {}", compId);
        return CompilationMapper.compilationToCompilationDto(compilation);
    }

    @Override
    public CompilationDto createCompilationForAdmin(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilationToSave = CompilationMapper.newCompilationDtoToCompilation(newCompilationDto, events);
        Compilation out = compilationRepository.save(compilationToSave);
        log.info("Сохранен Compilation id: {}, pinned: {}, title: {}", out.getId(),
                out.getPinned(), out.getTitle());
        return CompilationMapper.compilationToCompilationDto(out);
    }

    @Override
    public void deleteCompilationForAdmin(Long compId) {
        compilationRepository.findById(compId)
                .orElseThrow(() -> {
                    log.info("Не найдена Compilation с id: {} для удаления", compId);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
        compilationRepository.deleteById(compId);
        log.info("Успешно удалена Compilation с id: {}", compId);
    }

    @Override
    public CompilationDto updateCompilationForAdmin(NewCompilationDto newCompilationDto, Long compId) {
        Compilation compilation = compilationRepository.findById(compId)
                .orElseThrow(() -> {
                    log.info("Не найдена Compilation с id: {} для обновления", compId);
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                });

        if (newCompilationDto.getPinned() != null) {
            compilation.setPinned(newCompilationDto.getPinned());
        }
        if (newCompilationDto.getTitle() != null) {
            compilation.setTitle(newCompilationDto.getTitle());
        }
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());
        if (!events.isEmpty()) {
            compilation.setEvents(events);
        }
        log.info("Успешно обновлена Compilation с id: {}", compId);
        return CompilationMapper.compilationToCompilationDto(compilation);
    }
}