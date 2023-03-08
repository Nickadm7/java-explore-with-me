package ru.practicum.ewmservice.compilations.service;

import ru.practicum.ewmservice.compilations.dto.CompilationDto;
import ru.practicum.ewmservice.compilations.dto.InputCompilationDto;
import ru.practicum.ewmservice.compilations.dto.NewCompilationDto;

import java.util.List;

public interface CompilationService {
    List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size);

    CompilationDto getCompilationById(Long compId);

    CompilationDto createCompilationForAdmin(InputCompilationDto inputCompilationDto);

    void deleteCompilationForAdmin(Long compId);

    CompilationDto updateCompilationForAdmin(NewCompilationDto newCompilationDto, Long compId);
}
