package ru.practicum.categories.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.categories.dto.CategoryDto;
import ru.practicum.categories.model.Category;

@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CategoryMapper {
    public static CategoryDto categoryToCategoryDto(Category category) {
        return CategoryDto.builder()
                .id(category.getId())
                .name(category.getName())
                .build();
    }

    public static Category categoryDtoToCategory(CategoryDto categoryDto) {
        return Category.builder()
                .id(categoryDto.getId())
                .name(categoryDto.getName())
                .build();
    }
}