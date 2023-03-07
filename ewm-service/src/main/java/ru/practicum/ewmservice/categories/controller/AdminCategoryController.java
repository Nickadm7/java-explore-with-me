package ru.practicum.ewmservice.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.ewmservice.categories.dto.CategoryDto;
import ru.practicum.ewmservice.categories.service.CategoryService;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    @ResponseStatus(value = HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody CategoryDto categoryDto) {
        log.info("Post запрос к /admin/categories с параметрами category c name: {}", categoryDto.getName());
        return categoryService.createCategory(categoryDto);
    }

    @PatchMapping("/{catId}")
    public CategoryDto update(@PathVariable Long catId, @Valid @RequestBody CategoryDto categoryDto) {
        log.info("Patch запрос к /admin/categories с параметрами catId: {}", catId);
        return categoryService.updateCategory(catId, categoryDto);
    }

    @DeleteMapping("/{catId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long catId) {
        log.info("Delete запрос к /admin/categories с параметрами catId: {}", catId);
        categoryService.deleteCategory(catId);
    }
}