package ru.practicum.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.compilations.dto.CompilationDto;
import ru.practicum.compilations.dto.InputCompilationDto;
import ru.practicum.compilations.dto.NewCompilationDto;
import ru.practicum.compilations.service.CompilationService;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDto createCompilationForAdmin(@Validated @RequestBody InputCompilationDto inputCompilationDto) {
        log.info("Post запрос к /admin/compilations с параметрами events: {}, pinned: {}, title: {}",
                inputCompilationDto.getEvents(), inputCompilationDto.getPinned(), inputCompilationDto.getTitle());
        return compilationService.createCompilationForAdmin(inputCompilationDto);
    }

    @DeleteMapping(path = "/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteCompilationForAdmin(@PathVariable("compId") Long compId) {
        log.info("Delete запрос к /admin/compilations/{compId} с параметрами compId: {}", compId);
        compilationService.deleteCompilationForAdmin(compId);
    }

    @PatchMapping(path = "/{compId}")
    public CompilationDto updateCompilationForAdmin(@Validated @RequestBody NewCompilationDto newCompilationDto,
                                                    @PathVariable("compId") Long compId) {
        log.info("Patch запрос к /admin/compilations/{compId} с параметрами compId: {}, events: {}, pinned: {}, title: {}",
                compId, newCompilationDto.getEvents(), newCompilationDto.getPinned(), newCompilationDto.getTitle());
        return compilationService.updateCompilationForAdmin(newCompilationDto, compId);
    }
}