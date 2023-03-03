package ru.practicum.ewmservice.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.user.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByName(String name);
}