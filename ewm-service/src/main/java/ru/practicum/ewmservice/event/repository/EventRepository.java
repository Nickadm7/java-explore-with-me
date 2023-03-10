package ru.practicum.ewmservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.categories.model.Category;
import ru.practicum.ewmservice.event.model.Event;
import ru.practicum.ewmservice.user.model.User;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long>, QuerydslPredicateExecutor<Event> {
    List<Event> findAllByInitiator(User initiator);

    List<Event> findAllByCategory(Category category);
}