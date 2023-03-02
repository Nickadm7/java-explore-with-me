package ru.practicum.ewmservice.categories.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.ewmservice.categories.dto.CategoryDto;
import ru.practicum.ewmservice.categories.mapper.CategoryMapper;
import ru.practicum.ewmservice.categories.model.Category;
import ru.practicum.ewmservice.categories.repository.CategoryRepository;
import ru.practicum.ewmservice.exception.ConflictExistException;
import ru.practicum.ewmservice.exception.NotFoundParameterException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;

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
        Category out = categoryRepository.findById(catId)
                .orElseThrow(() -> {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                });
        log.info("Получена категория с catId: {}", catId);
        return CategoryMapper.categoryToCategoryDto(out);
    }

    @Override
    @Transactional
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category bufferToSave = CategoryMapper.categoryDtoToCategory(categoryDto);
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ConflictExistException("Категория с данным именем уже существует");
        }
        Category out = categoryRepository.save(bufferToSave);
        log.info("Категория с id: {} и name: {} сохранена", bufferToSave.getId(), bufferToSave.getName());
        return CategoryMapper.categoryToCategoryDto(out);
    }

    @Override
    public CategoryDto updateCategory(Long catId, CategoryDto categoryDto) {
        Category bufferToSave = categoryRepository.findById(catId)
                .orElseThrow(() -> {
                    throw new NotFoundParameterException("Не найдена category по catId");
                });
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new ConflictExistException("Категория с данным именем уже существует");
        }
        Category out = categoryRepository.save(bufferToSave);
        log.info("Категория с id: {} и name: {} обновлена", bufferToSave.getId(), bufferToSave.getName());
        return CategoryMapper.categoryToCategoryDto(out);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        categoryRepository.findById(catId)
                .orElseThrow(() -> {
                    throw new NotFoundParameterException("Не найдена category по catId");
                });
        categoryRepository.deleteById(catId);
        log.info("Категория с id: {} удалена", catId);
    }
}