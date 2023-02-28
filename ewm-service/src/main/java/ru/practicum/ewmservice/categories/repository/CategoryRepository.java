package ru.practicum.ewmservice.categories.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.ewmservice.categories.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByName(String name);
}