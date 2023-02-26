package ru.practicum.stats.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.ViewStats;
import ru.practicum.stats.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatsRepository extends JpaRepository<Hit, Long> {
    @Query(name = "findAllHitsNoUnique", nativeQuery = true)
    List<ViewStats> findAllHitsNoUnique(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(name = "findAllHitsUnique", nativeQuery = true)
    List<ViewStats> findAllHitsUnique(LocalDateTime start, LocalDateTime end, List<String> uris);
}