package ru.practicum.ewmservice.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.ewmservice.event.model.Location;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {
}
