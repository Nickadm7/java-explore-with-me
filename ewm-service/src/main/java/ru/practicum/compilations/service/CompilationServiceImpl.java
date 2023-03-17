package ru.practicum.compilations.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.InputCompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.mapper.CompilationMapper;
import ru.practicum.compilations.model.Compilation;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.compilations.repository.CompilationRepository;

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
        Compilation out = getCompilationByIdFromRepository(compId);
        log.info("Найдена Compilation с id: {}", compId);
        return CompilationMapper.compilationToCompilationDto(out);
    }

    @Override
    public CompilationDto createCompilationForAdmin(InputCompilationDto inputCompilationDto) {
        List<Event> events = eventRepository.findAllById(inputCompilationDto.getEvents());
        Compilation compilationToSave = CompilationMapper.newCompilationDtoToCompilation(inputCompilationDto, events);
        Compilation out = compilationRepository.save(compilationToSave);
        log.info("Сохранен Compilation id: {}, pinned: {}, title: {}", out.getId(),
                out.getPinned(), out.getTitle());
        return CompilationMapper.compilationToCompilationDto(out);
    }

    @Override
    public void deleteCompilationForAdmin(Long compId) {
        getCompilationByIdFromRepository(compId); //проверка что Compilation существует
        compilationRepository.deleteById(compId);
        log.info("Успешно удалена Compilation с id: {}", compId);
    }

    @Override
    public CompilationDto updateCompilationForAdmin(NewCompilationDto newCompilationDto, Long compId) {
        Compilation compilation = getCompilationByIdFromRepository(compId);
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
        compilationRepository.save(compilation);
        log.info("Успешно обновлена Compilation с id: {}", compId);
        return CompilationMapper.compilationToCompilationDto(compilation);
    }

    private Compilation getCompilationByIdFromRepository(Long compId) {
        return compilationRepository.findById(compId)
                .orElseThrow(() -> {
                    log.info("Не найдена Compilation с id: {}", compId);
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                });
    }
}