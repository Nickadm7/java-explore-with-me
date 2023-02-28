package ru.practicum.ewmservice.categories.service;

import ru.practicum.ewmservice.categories.dto.CategoryDto;

import java.util.List;

public interface CategoryService {
    List<CategoryDto> getAllCategories(Integer from, Integer size);

    CategoryDto getById(Long catId);
}
