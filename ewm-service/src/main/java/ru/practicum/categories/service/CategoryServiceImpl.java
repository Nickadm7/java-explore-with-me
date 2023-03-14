package ru.practicum.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.categories.repository.CategoryRepository;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.mapper.CategoryMapper;
import ru.practicum.categories.model.Category;
import ru.practicum.exception.ConflictException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        Pageable pageable = PageRequest.of(
                from / size,
                size,
                Sort.by(Sort.Direction.ASC, "id")
        );
        List<Category> out = categoryRepository.findAll(pageable).toList();
        log.info("Получен список всех категорий с from: {}, size: {}", from, size);
        return out.stream()
                .map(CategoryMapper::categoryToCategoryDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getById(Long catId) {
        Category out = getCategoryByIdFromRepository(catId);
        log.info("Получена категория с catId: {}", catId);
        return CategoryMapper.categoryToCategoryDto(out);
    }

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category bufferToSave = CategoryMapper.categoryDtoToCategory(categoryDto);
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ConflictException("Категория с данным именем уже существует");
        }
        Category out = categoryRepository.save(bufferToSave);
        log.info("Категория с id: {} и name: {} сохранена", bufferToSave.getId(), bufferToSave.getName());
        return CategoryMapper.categoryToCategoryDto(out);
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category bufferToSave = getCategoryByIdFromRepository(catId);
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ConflictException("Категория с данным именем уже существует");
        }
        Category out = categoryRepository.save(CategoryMapper.categoryDtoToCategory(categoryDto));
        log.info("Категория с id: {} и name: {} обновлена", bufferToSave.getId(), bufferToSave.getName());
        return CategoryMapper.categoryToCategoryDto(out);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        Category category = getCategoryByIdFromRepository(catId);
        if (!eventRepository.findAllByCategory(category).isEmpty()) {
            throw new ConflictException("Нельзя удалить категорию к ней привязаны Event");
        }
        categoryRepository.deleteById(catId);
        log.info("Категория с id: {} удалена", catId);
    }

    private Category getCategoryByIdFromRepository(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
    }
}