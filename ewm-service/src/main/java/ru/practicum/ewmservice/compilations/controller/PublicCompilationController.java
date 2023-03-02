package ru.practicum.ewmservice.compilations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.compilations.dto.CompilationDto;
import ru.practicum.ewmservice.compilations.service.CompilationService;

import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class PublicCompilationController {
    private final CompilationService compilationService;

    @GetMapping
    public List<CompilationDto> getAll(@RequestParam(required = false, defaultValue = "false") Boolean pinned,
                                       @RequestParam(defaultValue = "0") Integer from,
                                       @RequestParam(defaultValue = "10") Integer size) {
        log.info("Get запрос к /compilations с параметрами boolean: {}, from: {}, size: {}", pinned, from, size);
        return compilationService.getAllCompilations(pinned, from, size);
    }

    @GetMapping("/{compId}")
    public CompilationDto getById(@NotNull @PathVariable Long compId) {
        log.info("Get запрос к /compilations с параметрами compId: {}", compId);
        return compilationService.getCompilationById(compId);
    }
}