package ru.practicum.compilations.service;

import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.InputCompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);

    CompilationDto createCompilationForAdmin(InputCompilationDto inputCompilationDto);

    void deleteCompilationForAdmin(Long compId);

    CompilationDto updateCompilationForAdmin(NewCompilationDto newCompilationDto, Long compId);
}
