package ru.practicum.ewmservice.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.compilations.dto.CompilationDto;
import ru.practicum.ewmservice.compilations.dto.NewCompilationDto;
import ru.practicum.ewmservice.compilations.service.CompilationService;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationController {
    private final CompilationService compilationService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CompilationDto createCompilationForAdmin(@Validated @RequestBody NewCompilationDto newCompilationDto) {
        log.info("Post запрос к /admin/compilations с параметрами newCompilationDto: {}", newCompilationDto);
        return compilationService.createCompilationForAdmin(newCompilationDto);
    }

    @DeleteMapping(path = "/{compId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public CompilationDto deleteCompilationForAdmin(@PathVariable("compId") Long compId) {
        log.info("Delete запрос к /admin/compilations/{compId} с параметрами compId: {}", compId);
        return compilationService.deleteCompilationForAdmin(compId);
    }

    @PatchMapping(path = "/{compId}")
    public CompilationDto updateCompilationForAdmin(@Validated @RequestBody NewCompilationDto newCompilationDto,
                                 @PathVariable("compId") Long compId) {
        log.info("Patch запрос к /admin/compilations/{compId} с параметрами compId: {}", compId);
        return compilationService.updateCompilationForAdmin(newCompilationDto, compId);
    }
}